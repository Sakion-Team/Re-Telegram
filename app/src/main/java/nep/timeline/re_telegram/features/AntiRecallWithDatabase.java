package nep.timeline.re_telegram.features;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.HookInit;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.application.HostApplicationInfo;
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

public class AntiRecallWithDatabase {
    private static final Map<Integer, SQLiteDatabase> mDatabase = new HashMap<>(1);
    private static final CopyOnWriteArrayList<DeletedMessageInfo> shouldDeletedMessageInfo = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<DeletedMessageInfo> shouldDeletedMessageInfo2 = new CopyOnWriteArrayList<>();

    private static final Object lock = new Object();

    private static SQLiteDatabase ensureDatabase(int slot) {
        if (!(slot >= 0 && slot < Short.MAX_VALUE)) {
            throw new IllegalArgumentException("invalid slot: " + slot);
        }
        if (mDatabase.containsKey(slot)) {
            return mDatabase.get(slot);
        }
        File databaseFile = Utils.deletedMessagesDatabasePath;
        boolean createTable = !databaseFile.exists();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(
                databaseFile.getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE | (createTable ? SQLiteDatabase.CREATE_IF_NECESSARY : 0)
        );
        synchronized (lock) {
            database.beginTransaction();
            try {
                database.rawQuery("PRAGMA secure_delete = ON", null).close();
                database.rawQuery("PRAGMA temp_store = MEMORY", null).close();
                database.rawQuery("PRAGMA journal_mode = WAL", null).close();
                database.rawQuery("PRAGMA journal_size_limit = 10485760", null).close();
                database.rawQuery("PRAGMA busy_timeout = 5000", null).close();
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
        database.execSQL("""
                CREATE TABLE IF NOT EXISTS t_deleted_messages (
                  message_id INTEGER NOT NULL,
                  dialog_id INTEGER NOT NULL
                );""");
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_messages_combined ON t_deleted_messages (message_id, dialog_id)");
        mDatabase.put(slot, database);
        return database;
    }

    private static boolean messageIsDeleted(int messageId, long dialogId) {
        int currentSlot = UserConfig.getSelectedAccount();
        if (currentSlot < 0) {
            Utils.log("message_is_delete: no active account");
            return false;
        }
        SQLiteDatabase database = ensureDatabase(currentSlot);
        Cursor cursor = null;
        boolean result;

        try {
            String[] columns = {"message_id", "dialog_id"};
            String selection = "message_id = ? AND dialog_id = ?";
            String[] selectionArgs = {String.valueOf(messageId), String.valueOf(dialogId)};
            cursor = database.query("t_deleted_messages", columns, selection, selectionArgs, null, null, null);

            result = (cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }

    private static void insertDeletedMessage(ArrayList<Integer> messageIds, long dialogId) {
        int currentSlot = UserConfig.getSelectedAccount();
        if (currentSlot < 0) {
            Utils.log("message_is_delete: no active account");
            return;
        }
        SQLiteDatabase database = ensureDatabase(currentSlot);
        database.beginTransaction();
        try {
            for (Integer messageId : messageIds) {
                if (messageIsDeleted(messageId, dialogId))
                    continue;

                ContentValues values = new ContentValues();
                values.put("message_id", messageId);
                values.put("dialog_id", dialogId);

                database.insert("t_deleted_messages", null, values);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            if (!(e instanceof SQLiteConstraintException)) {
                Utils.log("failed to insert deleted message: " + e.getMessage());
            }
        } finally {
            database.endTransaction();
        }
    }

    private static DeletedMessageInfo isShouldDeletedMessage(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : shouldDeletedMessageInfo)
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

    public static void initUI(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> chatMessageCell = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Cells.ChatMessageCell"), lpparam.classLoader);

        if (chatMessageCell != null) {
            HookUtils.findAndHookMethod(chatMessageCell, AutomationResolver.resolve("ChatMessageCell", "measureTime", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (Configs.isAntiRecall()) {
                        String text = Language.resolve(HostApplicationInfo.getApplication().getResources().getConfiguration().locale, "antirecall.message.deleted");
                        MessageObject messageObject = new MessageObject(param.args[0]);
                        TLRPC.Message owner = messageObject.getMessageOwner();
                        int id = owner.getID();
                        long channel_id = -owner.getPeerID().getChannelID();
                        if (messageIsDeleted(id, channel_id)) {
                            if (getCurrentTimeStringClassName(param.thisObject).equals("SpannableStringBuilder")) {
                                NekoChatMessageCell cell = new NekoChatMessageCell(param.thisObject);
                                SpannableStringBuilder time = cell.getCurrentTimeString();
                                String delta = "(" + text + ") ";
                                SpannableStringBuilder newDelta = new SpannableStringBuilder();
                                newDelta.append(delta).append(time);
                                time = newDelta;
                                cell.setCurrentTimeString(time);
                                TextPaint paint = Theme.getTextPaint();
                                if (paint != null)
                                {
                                    int deltaWidth = (int) Math.ceil(paint.measureText(delta));
                                    cell.setTimeTextWidth(deltaWidth + cell.getTimeTextWidth());
                                    cell.setTimeWidth(deltaWidth + cell.getTimeWidth());
                                }
                            } else {
                                OfficialChatMessageCell cell = new OfficialChatMessageCell(param.thisObject);
                                String time = (String) cell.getCurrentTimeString();
                                String delta = "(" + text + ") ";
                                time = delta + time;
                                cell.setCurrentTimeString(time);
                                TextPaint paint = Theme.getTextPaint();
                                if (paint != null)
                                {
                                    int deltaWidth = (int) Math.ceil(paint.measureText(delta));
                                    cell.setTimeTextWidth(deltaWidth + cell.getTimeTextWidth());
                                    cell.setTimeWidth(deltaWidth + cell.getTimeWidth());
                                }
                            }
                        }
                    }
                }
            });
        } else {
            Utils.log("Not found ChatMessageCell, " + Utils.issue);
        }
    }

    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), lpparam.classLoader);
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

                XposedHelpers.findAndHookMethod(messagesController, methodName, ArrayList.class, ArrayList.class, ArrayList.class, boolean.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Configs.isAntiRecall())
                        {
                            Class<?> TL_updateDeleteMessages = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$TL_updateDeleteMessages"));
                            Class<?> TL_updateDeleteChannelMessages = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$TL_updateDeleteChannelMessages"));
                            //Class<?> TL_updateDeleteScheduledMessages = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.tgnet.TLRPC$TL_updateDeleteScheduledMessages"));
                            CopyOnWriteArrayList<Object> updates = new CopyOnWriteArrayList<>(Utils.castList(param.args[0], Object.class));
                            if (updates != null && !updates.isEmpty()) {
                                //ArrayList<Object> newUpdates = new ArrayList<>();

                                for (Object item : updates) {
                                    //if (!item.getClass().equals(TL_updateDeleteChannelMessages) && !item.getClass().equals(TL_updateDeleteMessages))// && !item.getClass().equals(TL_updateDeleteScheduledMessages))
                                    //newUpdates.add(item);

                                    //if (item.getClass().equals(TL_updateDeleteScheduledMessages))
                                    //    AntiRecall.insertDeletedMessage(new TLRPC.TL_updateDeleteScheduledMessages(item).getMessages());

                                    if (item.getClass().equals(TL_updateDeleteChannelMessages))
                                    {
                                        TLRPC.TL_updateDeleteChannelMessages channelMessages = new TLRPC.TL_updateDeleteChannelMessages(item);
                                        insertDeletedMessage(channelMessages.getMessages(), -channelMessages.getChannelID());
                                    }

                                    if (item.getClass().equals(TL_updateDeleteMessages))
                                        insertDeletedMessage(new TLRPC.TL_updateDeleteMessages(item).getMessages(), DeletedMessageInfo.NOT_CHANNEL);

                                    if (HookInit.DEBUG_MODE && (item.getClass().equals(TL_updateDeleteMessages) || item.getClass().equals(TL_updateDeleteChannelMessages)))
                                        Utils.log("Protected message! event: " + item.getClass());
                                }

                                //param.args[0] = newUpdates;
                            }
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

    public static void initProcessing(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class<?> messagesStorage = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.MessagesStorage"));
        Class<?> notificationCenter = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationCenter"));
        Class<?> notificationsController = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationsController"));

        List<Method> markMessagesAsDeletedMethods = new ArrayList<>();
        for (Method method : messagesStorage.getDeclaredMethods()) {
            if (method.getName().equals(AutomationResolver.resolve("MessagesStorage", "markMessagesAsDeleted", AutomationResolver.ResolverType.Method)) || method.getName().equals(AutomationResolver.resolve("MessagesStorage", "markMessagesAsDeleted2", AutomationResolver.ResolverType.Method))) {
                markMessagesAsDeletedMethods.add(method);
            }
        }

        if (markMessagesAsDeletedMethods.isEmpty()) {
            Utils.log("Failed to hook markMessagesAsDeleted! Reason: No method found, " + Utils.issue);
            return;
        }

        for (Method markMessagesAsDeletedMethod : markMessagesAsDeletedMethods) {
            XposedBridge.hookMethod(markMessagesAsDeletedMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (Configs.isAntiRecall() && param.args[1] instanceof ArrayList) {
                        ArrayList<Integer> deletedMessages = new ArrayList<>();

                        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(Utils.castList(param.args[1], Integer.class));
                        if (list.isEmpty())
                            return;
                        long channel_id = (long) param.args[0];
                        if (channel_id > 0)
                            channel_id = 0;

                        for (Integer msgId : list) {
                            DeletedMessageInfo shouldDeletedMessage = isShouldDeletedMessage(channel_id, msgId);
                            DeletedMessageInfo shouldDeletedMessage2 = isShouldDeletedMessage2(channel_id, msgId);
                            if (shouldDeletedMessage2 != null || !messageIsDeleted(msgId, channel_id)) {
                                deletedMessages.add(msgId);
                                if (shouldDeletedMessage != null)
                                    shouldDeletedMessageInfo.remove(shouldDeletedMessage);
                                if (shouldDeletedMessage2 != null)
                                    shouldDeletedMessageInfo2.remove(shouldDeletedMessage2);
                            }
                        }

                        ((ArrayList<Integer>) param.args[1]).clear();
                        ((ArrayList<Integer>) param.args[1]).addAll(deletedMessages);
                    }
                }
            });
        }

        List<Method> updateDialogsWithDeletedMessagesMethods = new ArrayList<>();
        for (Method method : messagesStorage.getDeclaredMethods()) {
            if (method.getName().equals(AutomationResolver.resolve("MessagesStorage", "updateDialogsWithDeletedMessages", AutomationResolver.ResolverType.Method))) {
                updateDialogsWithDeletedMessagesMethods.add(method);
            }
        }

        if (updateDialogsWithDeletedMessagesMethods.isEmpty()) {
            Utils.log("Failed to hook updateDialogsWithDeletedMessages! Reason: No method found, " + Utils.issue);
            return;
        }

        for (Method updateDialogsWithDeletedMessagesMethod : updateDialogsWithDeletedMessagesMethods) {
            XposedBridge.hookMethod(updateDialogsWithDeletedMessagesMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (param.args[2] instanceof ArrayList)
                    {
                        long channelID = -((long) param.args[1]);
                        if (channelID > 0)
                            channelID = 0;

                        ArrayList<Integer> deletedMessages = new ArrayList<>();

                        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(Utils.castList(param.args[2], Integer.class));
                        if (!list.isEmpty())
                            for (Integer msgId : list) {
                                if (isShouldDeletedMessage(channelID, msgId) == null)
                                    if (messageIsDeleted(msgId, channelID))
                                        addShouldDeletedMessage(channelID, msgId);
                                    else
                                        deletedMessages.remove(msgId);
                                else if (isShouldDeletedMessage2(channelID, msgId) == null)
                                    if (messageIsDeleted(msgId, channelID))
                                        addShouldDeletedMessage2(channelID, msgId);
                                    else
                                        deletedMessages.remove(msgId);
                                else
                                    deletedMessages.add(msgId);
                            }

                        param.args[2] = deletedMessages;
                        //param.setResult(null);
                    }
                }
            });
        }

        int messagesDeletedValue = (int) XposedHelpers.getStaticObjectField(notificationCenter, AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field));

        HookUtils.findAndHookMethod(notificationCenter, AutomationResolver.resolve("NotificationCenter", "postNotificationName", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                int id = (int) param.args[0];
                if (Configs.isAntiRecall() && id == messagesDeletedValue) {
                    Object[] args = (Object[]) param.args[1];
                    long dialogID = (long) args[1];
                    ArrayList<Integer> arrayList = Utils.castList(args[0], Integer.class);
                    param.setResult(null);
                    insertDeletedMessage(arrayList, dialogID);
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
            XposedBridge.hookMethod(removeDeletedMessagesFromNotifications, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (Configs.isAntiRecall())
                        param.setResult(null);
                }
            });
    }
}
