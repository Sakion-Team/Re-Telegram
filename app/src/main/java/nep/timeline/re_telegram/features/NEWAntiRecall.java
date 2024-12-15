package nep.timeline.re_telegram.features;

import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.HookInit;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.application.HostApplicationInfo;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.language.Language;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.structs.DeletedMessageInfo;
import nep.timeline.re_telegram.utils.FieldUtils;
import nep.timeline.re_telegram.virtuals.MessageObject;
import nep.timeline.re_telegram.virtuals.OfficialChatMessageCell;
import nep.timeline.re_telegram.virtuals.TLRPC;
import nep.timeline.re_telegram.virtuals.Theme;
import nep.timeline.re_telegram.virtuals.UserConfig;
import nep.timeline.re_telegram.virtuals.nekogram.NekoChatMessageCell;

public class NEWAntiRecall {
    private static long lastVisibleTime = -1;
    private static MessageObject currentMessageObject;
    private static final Handler storage = new Handler(makeLooper("Storage"));

    private static final CopyOnWriteArraySet<DeletedMessageInfo> deletedMessageInfos = new CopyOnWriteArraySet<>();
    private static final CopyOnWriteArraySet<DeletedMessageInfo> shouldDeletedMessageInfo = new CopyOnWriteArraySet<>();
    private static final CopyOnWriteArraySet<DeletedMessageInfo> shouldDeletedMessageInfo2 = new CopyOnWriteArraySet<>();

    public static final int FLAG_DELETED = 1 << 31;

    public static Looper makeLooper(String str) {
        HandlerThread handlerThread = new HandlerThread("ReTelegram - " + str, Process.THREAD_PRIORITY_DISPLAY);
        handlerThread.start();
        return handlerThread.getLooper();
    }

    public static void markMessagesDeleted(Object thisMessagesStorage, final long dialogId, ArrayList<Integer> delMsg) {
        deletedMessageInfos.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), dialogId, delMsg));
        storage.post(() -> {
            Object db = XposedHelpers.callMethod(thisMessagesStorage, AutomationResolver.resolve("MessagesStorage", "getDatabase", AutomationResolver.ResolverType.Method));
            String query = "SELECT data,mid,uid " +
                    "FROM messages_v2 " +
                    "WHERE " + (dialogId == 0 ? "is_channel" : "uid") + " = " + dialogId + " AND mid IN (" + TextUtils.join(",", delMsg) + ");";

            String update = "UPDATE messages_v2 SET data = ? WHERE uid = ? AND mid = ?";
            Object cursor = XposedHelpers.callMethod(db, AutomationResolver.resolve("SQLiteDatabase", "queryFinalized", AutomationResolver.ResolverType.Method), new Class<?>[] { String.class, Object[].class }, query, new Object[] { });
            Object state = XposedHelpers.callMethod(db, AutomationResolver.resolve("SQLiteDatabase", "executeFast", AutomationResolver.ResolverType.Method), new Class<?>[] { String.class }, update);

            while ( (boolean) XposedHelpers.callMethod(cursor, AutomationResolver.resolve("SQLiteCursor", "next", AutomationResolver.ResolverType.Method))) {
                Object data = XposedHelpers.callMethod(cursor, AutomationResolver.resolve("SQLiteCursor", "byteBufferValue", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class }, 0);
                int mid = (int) XposedHelpers.callMethod(cursor, AutomationResolver.resolve("SQLiteCursor", "intValue", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class }, 1);
                long lastDialogId = (long) XposedHelpers.callMethod(cursor, AutomationResolver.resolve("SQLiteCursor", "longValue", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class }, 2);

                XposedHelpers.callMethod(data, "position", new Class<?>[] { int.class }, 4);
                int flags = (int) XposedHelpers.callMethod(data, "readInt32", new Class<?>[] { boolean.class }, true);
                flags |= FLAG_DELETED;
                XposedHelpers.callMethod(data, "position", new Class<?>[] { int.class }, 4);
                XposedHelpers.callMethod(data, "writeInt32", new Class<?>[] { int.class }, flags);
                XposedHelpers.callMethod(data, "position", new Class<?>[] { int.class }, 0);

                XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "requery", AutomationResolver.ResolverType.Method));
                XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "bindByteBuffer", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class, data.getClass() }, 1, data);
                XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "bindLong", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class, long.class }, 2, lastDialogId);
                XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "bindInteger", AutomationResolver.ResolverType.Method), new Class<?>[] { int.class, int.class }, 3, mid);
                XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "step", AutomationResolver.ResolverType.Method));

                XposedHelpers.callMethod(data, "reuse");
            }
            XposedHelpers.callMethod(cursor, AutomationResolver.resolve("SQLiteCursor", "dispose", AutomationResolver.ResolverType.Method));
            XposedHelpers.callMethod(state, AutomationResolver.resolve("SQLitePreparedStatement", "dispose", AutomationResolver.ResolverType.Method));
        });
    }

    public static void markMessagesDeletedForController(Object thisMessagesController, long dialogId, ArrayList<Integer> delMsg) {
        markMessagesDeleted(XposedHelpers.callMethod(thisMessagesController, "getMessagesStorage"), dialogId, delMsg);
    }

    private static DeletedMessageInfo isShouldDeletedMessage(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : shouldDeletedMessageInfo)
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
                return deletedMessagesId;
        return null;
    }

    private static DeletedMessageInfo isDeletedMessage(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : deletedMessageInfos)
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
                return deletedMessagesId;
        return null;
    }

    private static DeletedMessageInfo isShouldDeletedMessage2(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : shouldDeletedMessageInfo2)
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
                return deletedMessagesId;
        return null;
    }

    private static void addShouldDeletedMessage(long channelID, Integer messageId) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : shouldDeletedMessageInfo) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            shouldDeletedMessageInfo.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), channelID, messageId));
        else
        {
            if (!info.getMessageIds().contains(messageId)) // No duplication
                info.insertMessageId(messageId);
        }
    }

    private static void addShouldDeletedMessage2(long channelID, Integer messageId) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : shouldDeletedMessageInfo2) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            shouldDeletedMessageInfo2.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), channelID, messageId));
        else
        {
            if (!info.getMessageIds().contains(messageId)) // No duplication
                info.insertMessageId(messageId);
        }
    }

    private static String getCurrentTimeStringClassName(Object chatMessageCellInstance)
    {
        Object currentTimeString = FieldUtils.getFieldClassOfClass(chatMessageCellInstance, "currentTimeString");
        assert currentTimeString != null;
        return currentTimeString.getClass().getSimpleName();
    }

    public static SpannableStringBuilder convertToStringBuilder(CharSequence charSequence) {
        if (charSequence != null)
            return charSequence instanceof SpannableStringBuilder ? (SpannableStringBuilder) charSequence : new SpannableStringBuilder(charSequence);
        else
            return null;
    }

    public static void initUI(ClassLoader classLoader)
    {
        Class<?> chatMessageCell = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Cells.ChatMessageCell"), classLoader);
        if (chatMessageCell != null) {
            /*XposedHelpers.findAndHookMethod(chatMessageCell, AutomationResolver.resolve("ChatMessageCell", "setVisibleOnScreen", AutomationResolver.ResolverType.Method), boolean.class, float.class, float.class, new AbstractMethodHook() {
                @Override
                protected void afterMethod(MethodHookParam param) {
                    if (Configs.isAntiRecall()) {
                        boolean visible = (boolean) param.args[0];
                        if (visible)
                            currentMessageObject = new MessageObject(XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("ChatMessageCell", "currentMessageObject", AutomationResolver.ResolverType.Field)));
                    }
                }
            });*/

            XposedHelpers.findAndHookMethod(chatMessageCell, AutomationResolver.resolve("ChatMessageCell", "measureTime", AutomationResolver.ResolverType.Method), AutomationResolver.resolve("org.telegram.messenger.MessageObject"), new AbstractMethodHook() {
                @Override
                protected void afterMethod(MethodHookParam param) {
                    try {
                        if (Configs.isAntiRecall()) {
                            currentMessageObject = new MessageObject(XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("ChatMessageCell", "currentMessageObject", AutomationResolver.ResolverType.Field)));
                            lastVisibleTime = System.currentTimeMillis();
                            String text = Configs.getAntiRecallText().isEmpty() ? ("(" + Language.resolve(HostApplicationInfo.getApplication().getResources().getConfiguration().locale, "antirecall.message.deleted") + ")") : Configs.getAntiRecallText();
                            Object msgObj = param.args[0];
                            if (msgObj == null)
                                return;
                            MessageObject messageObject = new MessageObject(msgObj);
                            if (messageObject == null)
                                return;
                            TLRPC.Message owner = messageObject.getMessageOwner();
                            if (owner == null)
                                return;
                            int flags = owner.getFlags();
                            if ((flags & FLAG_DELETED) != 0) {
                                if (getCurrentTimeStringClassName(param.thisObject).equals("SpannableStringBuilder")) {
                                    NekoChatMessageCell cell = new NekoChatMessageCell(param.thisObject);
                                    SpannableStringBuilder time = cell.getCurrentTimeString();
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                                    if (Configs.isAntiRecallTextColorful())
                                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(Configs.getAntiRecallTextRed(), Configs.getAntiRecallTextGreen(), Configs.getAntiRecallTextBlue())), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableStringBuilder.append(" ");
                                    time.insert(0, spannableStringBuilder);
                                    cell.setCurrentTimeString(time);
                                    TextPaint paint = Theme.getTextPaint(classLoader);
                                    if (paint != null) {
                                        int ceil = (int) Math.ceil(paint.measureText(spannableStringBuilder, 0, spannableStringBuilder.length()));
                                        cell.setTimeTextWidth(ceil + cell.getTimeTextWidth());
                                        cell.setTimeWidth(ceil + cell.getTimeWidth());
                                    }
                                } else {
                                    OfficialChatMessageCell cell = new OfficialChatMessageCell(param.thisObject);
                                    SpannableStringBuilder time = convertToStringBuilder(cell.getCurrentTimeString());
                                    if (time == null)
                                        return;
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                                    if (Configs.isAntiRecallTextColorful())
                                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.rgb(Configs.getAntiRecallTextRed(), Configs.getAntiRecallTextGreen(), Configs.getAntiRecallTextBlue())), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableStringBuilder.append(" ");
                                    time.insert(0, spannableStringBuilder);
                                    cell.setCurrentTimeString(time);
                                    TextPaint paint = Theme.getTextPaint(classLoader);
                                    if (paint != null) {
                                        int ceil = (int) Math.ceil(paint.measureText(spannableStringBuilder, 0, spannableStringBuilder.length()));
                                        cell.setTimeTextWidth(ceil + cell.getTimeTextWidth());
                                        cell.setTimeWidth(ceil + cell.getTimeWidth());
                                    }
                                }
                            } else {
                                TextPaint paint = Theme.getTextPaint(classLoader);
                                paint.setShadowLayer(0, 0, 0, Color.WHITE);
                            }
                        }
                    } catch (Throwable throwable) {
                        Utils.log(throwable);
                    }
                }
            });
        } else {
            Utils.log("Not found ChatMessageCell, " + Utils.issue);
        }
    }

    public static void init(ClassLoader classLoader)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), classLoader);
        if (messagesController != null) {
            Method[] messagesControllerMethods = messagesController.getDeclaredMethods();
            List<String> methodNames = new ArrayList<>();

            for (Method method : messagesControllerMethods)
                if (method.getParameterCount() == 5 && method.getParameterTypes()[0] == ArrayList.class && method.getParameterTypes()[1] == ArrayList.class && method.getParameterTypes()[2] == ArrayList.class && method.getParameterTypes()[3] == boolean.class && method.getParameterTypes()[4] == int.class)
                    methodNames.add(method.getName());

            if (methodNames.size() != 1)
                Utils.log("Failed to hook processUpdateArray! Reason: " + (methodNames.isEmpty() ? "No method found" : "Multiple methods found") + ", " + Utils.issue);
            else {
                String methodName = methodNames.get(0);

                XposedHelpers.findAndHookMethod(messagesController, methodName, ArrayList.class, ArrayList.class, ArrayList.class, boolean.class, int.class, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        try {
                            if (Configs.isAntiRecall())
                            {
                                Class<?> TL_updateDeleteMessages = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$TL_updateDeleteMessages"), classLoader);
                                Class<?> TL_updateDeleteChannelMessages = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$TL_updateDeleteChannelMessages"), classLoader);
                                CopyOnWriteArrayList<Object> updates = new CopyOnWriteArrayList<>(Utils.castList(param.args[0], Object.class));
                                if (updates != null && !updates.isEmpty()) {
                                    ArrayList<Object> newUpdates = new ArrayList<>();

                                    for (Object item : updates) {
                                        if (!item.getClass().equals(TL_updateDeleteChannelMessages) && !item.getClass().equals(TL_updateDeleteMessages))
                                            newUpdates.add(item);

                                        if (item.getClass().equals(TL_updateDeleteChannelMessages)) {
                                            TLRPC.TL_updateDeleteChannelMessages channelMessages = new TLRPC.TL_updateDeleteChannelMessages(item);

                                            Object dialogMessage = XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("MessagesController", "dialogMessage", AutomationResolver.ResolverType.Field));
                                            ArrayList<Object> dialogMessages = (ArrayList<Object>) XposedHelpers.callMethod(dialogMessage, AutomationResolver.resolve("LongSparseArray", "get", AutomationResolver.ResolverType.Method), new Class<?>[]{int.class}, -channelMessages.getChannelID());
                                            if (dialogMessages != null) {
                                                for (final Object msgObj : dialogMessages) {
                                                    TLRPC.Message owner = new MessageObject(msgObj).getMessageOwner();
                                                    if (channelMessages.getMessages().contains(owner.getID())) {
                                                        owner.setFlags(owner.getFlags() | FLAG_DELETED);
                                                    }
                                                }
                                            }

                                            markMessagesDeletedForController(param.thisObject, -channelMessages.getChannelID(), channelMessages.getMessages());
                                        }

                                        if (item.getClass().equals(TL_updateDeleteMessages)) {
                                            ArrayList<Integer> messages = new TLRPC.TL_updateDeleteMessages(item).getMessages();
                                            SparseArray<Object> dialogMessages = (SparseArray<Object>) XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("MessagesController", "dialogMessagesByIds", AutomationResolver.ResolverType.Field));
                                            for (int id : messages) {
                                                Object msgObj = dialogMessages.get(id);
                                                if (msgObj == null) {
                                                    break;
                                                } else {
                                                    TLRPC.Message owner = new MessageObject(msgObj).getMessageOwner();
                                                    owner.setFlags(owner.getFlags() | FLAG_DELETED);
                                                }
                                            }
                                            markMessagesDeletedForController(param.thisObject, DeletedMessageInfo.NOT_CHANNEL, messages);
                                        }

                                        if (HookInit.DEBUG_MODE && (item.getClass().equals(TL_updateDeleteMessages) || item.getClass().equals(TL_updateDeleteChannelMessages)))
                                            Utils.log("Protected message! event: " + item.getClass());
                                    }
                                    param.args[0] = newUpdates;
                                }
                            }
                        } catch (Throwable throwable) {
                            Utils.log(throwable);
                        }
                    }
                });
            }
        }
        else
        {
            Utils.log("Not found MessagesController, " + Utils.issue);
        }
    }

    public static void initProcessing(ClassLoader classLoader) {
        Class<?> messagesStorage = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesStorage"), classLoader);
        Class<?> notificationsController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.NotificationsController"), classLoader);

        XposedHelpers.findAndHookMethod(messagesStorage, AutomationResolver.resolve("MessagesStorage", "markMessagesAsDeletedInternal", AutomationResolver.ResolverType.Method), long.class, ArrayList.class, boolean.class, int.class, int.class, new AbstractMethodHook() {
            @Override
            protected void beforeMethod(MethodHookParam param) {
                if (Configs.isAntiRecall()) {
                    long dialogId = (long) param.args[0];
                    if (currentMessageObject != null && (System.currentTimeMillis() - lastVisibleTime) < 4000) {
                        long objectId = currentMessageObject.getDialogId();
                        if (objectId == dialogId)
                            return;
                    }

                    ArrayList<Integer> deletedMessages = new ArrayList<>();

                    ArrayList<Integer> original = Utils.castList(param.args[1], Integer.class);

                    if (original.isEmpty())
                        return;

                    long channel_id = (long) param.args[0];
                    if (channel_id > 0)
                        channel_id = 0;

                    for (Integer msgId : original) {
                        DeletedMessageInfo shouldDeletedMessage = isShouldDeletedMessage(channel_id, msgId);
                        DeletedMessageInfo shouldDeletedMessage2 = isShouldDeletedMessage2(channel_id, msgId);
                        if (shouldDeletedMessage2 != null || isDeletedMessage(channel_id, msgId) == null) {
                            deletedMessages.add(msgId);
                            if (shouldDeletedMessage != null)
                                shouldDeletedMessageInfo.remove(shouldDeletedMessage);
                            if (shouldDeletedMessage2 != null)
                                shouldDeletedMessageInfo2.remove(shouldDeletedMessage2);
                        }
                    }

                    ArrayList<Integer> fork = new ArrayList<>(original);
                    fork.removeAll(deletedMessages);
                    if (original != null && !fork.isEmpty())
                        markMessagesDeleted(param.thisObject, channel_id, fork);

                    if (deletedMessages.isEmpty())
                        param.setResult(null);
                    else {
                        ((ArrayList<Integer>) param.args[1]).clear();
                        ((ArrayList<Integer>) param.args[1]).addAll(deletedMessages);
                    }
                }
            }
        });

        XposedHelpers.findAndHookMethod(messagesStorage, AutomationResolver.resolve("MessagesStorage", "updateDialogsWithDeletedMessagesInternal", AutomationResolver.ResolverType.Method), long.class, long.class, ArrayList.class, ArrayList.class, new AbstractMethodHook() {
            @Override
            protected void beforeMethod(MethodHookParam param) {
                if (Configs.isAntiRecall()) {
                    long dialogId = (long) param.args[0];
                    if (currentMessageObject != null && (System.currentTimeMillis() - lastVisibleTime) < 4000) {
                        long objectId = currentMessageObject.getDialogId();
                        if (objectId == dialogId)
                            return;
                    }

                    long channelID = -((long) param.args[1]);
                    if (channelID > 0)
                        channelID = 0;

                    ArrayList<Integer> deletedMessages = new ArrayList<>();

                    Object msgIds = param.args[2];
                    if (msgIds == null)
                        return;

                    ArrayList<Integer> original = Utils.castList(msgIds, Integer.class);

                    if (original.isEmpty())
                        return;

                    for (Integer msgId : original)
                        if (isShouldDeletedMessage(channelID, msgId) == null)
                            if (isDeletedMessage(channelID, msgId) != null)
                                addShouldDeletedMessage(channelID, msgId);
                            else
                                deletedMessages.remove(msgId);
                        else if (isShouldDeletedMessage2(channelID, msgId) == null)
                            if (isDeletedMessage(channelID, msgId) != null)
                                addShouldDeletedMessage2(channelID, msgId);
                            else
                                deletedMessages.remove(msgId);
                        else
                            deletedMessages.add(msgId);

                    ArrayList<Integer> fork = new ArrayList<>(original);
                    fork.removeAll(deletedMessages);
                    if (original != null && !fork.isEmpty())
                        markMessagesDeleted(param.thisObject, channelID, fork);

                    if (deletedMessages.isEmpty())
                        param.setResult(null);
                    else {
                        ((ArrayList<Integer>) param.args[2]).clear();
                        ((ArrayList<Integer>) param.args[2]).addAll(deletedMessages);
                    }
                }
            }
        });

        Method updateDialogsWithDeletedMessagesMethod = null;
        for (Method method : messagesStorage.getDeclaredMethods()) {
            if (method.getName().equals(AutomationResolver.resolve("MessagesStorage", "updateDialogsWithDeletedMessages", AutomationResolver.ResolverType.Method)) && Objects.equals(method.getParameterTypes()[2], ArrayList.class)) {
                updateDialogsWithDeletedMessagesMethod = method;
            }
        }

        if (updateDialogsWithDeletedMessagesMethod == null) {
            Utils.log("Failed to hook updateDialogsWithDeletedMessages! Reason: No method found, " + Utils.issue);
            return;
        }

        XposedBridge.hookMethod(updateDialogsWithDeletedMessagesMethod, new AbstractMethodHook() {
            @Override
            protected void beforeMethod(MethodHookParam param) {
                if (Configs.isAntiRecall()) {
                    long dialogId = (long) param.args[0];
                    if (currentMessageObject != null && (System.currentTimeMillis() - lastVisibleTime) < 4000) {
                        long objectId = currentMessageObject.getDialogId();
                        if (objectId == dialogId)
                            return;
                    }

                    long channelID = -((long) param.args[1]);
                    if (channelID > 0)
                        channelID = 0;

                    ArrayList<Integer> deletedMessages = new ArrayList<>();

                    Object msgIds = param.args[2];
                    if (msgIds == null)
                        return;

                    ArrayList<Integer> original = Utils.castList(msgIds, Integer.class);

                    if (original.isEmpty())
                        return;

                    for (Integer msgId : original) {
                        if (isShouldDeletedMessage(channelID, msgId) == null)
                            if (isDeletedMessage(channelID, msgId) != null)
                                addShouldDeletedMessage(channelID, msgId);
                            else
                                deletedMessages.remove(msgId);
                        else if (isShouldDeletedMessage2(channelID, msgId) == null)
                            if (isDeletedMessage(channelID, msgId) != null)
                                addShouldDeletedMessage2(channelID, msgId);
                            else
                                deletedMessages.remove(msgId);
                        else
                            deletedMessages.add(msgId);
                    }

                    ArrayList<Integer> fork = new ArrayList<>(original);
                    fork.removeAll(deletedMessages);
                    if (original != null && !fork.isEmpty())
                        markMessagesDeleted(param.thisObject, channelID, fork);

                    if (deletedMessages.isEmpty())
                        param.setResult(null);
                    else {
                        ((ArrayList<Integer>) param.args[2]).clear();
                        ((ArrayList<Integer>) param.args[2]).addAll(deletedMessages);
                    }
                }
            }
        });

        Method removeDeletedMessagesFromNotifications = null;
        for (Method method : notificationsController.getDeclaredMethods())
            if (method.getName().equals(AutomationResolver.resolve("NotificationsController", "removeDeletedMessagesFromNotifications", AutomationResolver.ResolverType.Method)))
                removeDeletedMessagesFromNotifications = method;

        if (removeDeletedMessagesFromNotifications == null)
            Utils.log("Failed to hook removeDeletedMessagesFromNotifications! Reason: No method found, " + Utils.issue);
        else
            XposedBridge.hookMethod(removeDeletedMessagesFromNotifications, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isAntiRecall())
                        param.setResult(null);
                }
            });
    }
}