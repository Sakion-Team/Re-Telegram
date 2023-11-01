package nep.timeline.re_telegram.features;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

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

public class AntiRecall {
    private static final CopyOnWriteArrayList<DeletedMessageInfo> deletedMessagesIds = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<DeletedMessageInfo> deletedMessages2Ids = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<DeletedMessageInfo> needProcessing = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<DeletedMessageInfo> getDeletedMessagesIds() {
        return deletedMessagesIds;
    }

    public static DeletedMessageInfo messageIsDeleted(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
            {
                return deletedMessagesId;
            }
        }
        return null; // deletedMessagesIds.contains(messageId);
    }

    public static DeletedMessageInfo messageIsDeleted2(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : deletedMessages2Ids) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
            {
                return deletedMessagesId;
            }
        }
        return null; // deletedMessagesIds.contains(messageId);
    }

    public static DeletedMessageInfo findInNeedProcess(long channelID, int messageId) {
        for (DeletedMessageInfo deletedMessagesId : needProcessing) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
            {
                return deletedMessagesId;
            }
        }
        return null;
    }

    public static void insertDeletedMessage(long channelID, CopyOnWriteArrayList<Integer> messageIds) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            deletedMessagesIds.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), channelID, messageIds));
        else
        {
            for (Integer messageId : messageIds)
                if (!info.getMessageIds().contains(messageId)) // No duplication
                    info.insertMessageId(messageId);
        }
        Utils.saveDeletedMessages();
    }

    public static void insertDeletedMessage(long channelID, DeletedMessageInfo messageInfo) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == messageInfo.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            deletedMessagesIds.add(new DeletedMessageInfo(messageInfo.getSelectedAccount(), channelID, messageInfo.getMessageIds()));
        else
        {
            for (Integer messageId : messageInfo.getMessageIds())
                if (!info.getMessageIds().contains(messageId)) // No duplication
                    info.insertMessageId(messageId);
        }
        Utils.saveDeletedMessages();
    }

    public static void removeDeletedMessage(long channelID, Integer messageId) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (!needInit)
        {
            if (info.getMessageIds().contains(messageId))
                info.removeMessageId(messageId);
            Utils.saveDeletedMessages();
        }
    }

    public static void insertNeedProcessDeletedMessage(long channelID, CopyOnWriteArrayList<Integer> messageIds) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : needProcessing) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            needProcessing.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), channelID, messageIds));
        else
        {
            for (Integer messageId : messageIds)
                if (!info.getMessageIds().contains(messageId)) // No duplication
                    info.insertMessageIds(messageIds);
        }
    }

    public static void insertNeedProcessDeletedMessage(long channelID, Integer messageId) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : needProcessing) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            needProcessing.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), channelID, messageId));
        else
        {
            if (!info.getMessageIds().contains(messageId)) // No duplication
                info.insertMessageId(messageId);
        }
    }

    public static void insertDeletedMessageFromSaveFile(int selectedAccount, long channelID, CopyOnWriteArrayList<Integer> messageIds) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == selectedAccount && deletedMessagesId.getChannelID() == channelID)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            deletedMessagesIds.add(new DeletedMessageInfo(selectedAccount, channelID, messageIds));
        else
        {
            for (Integer messageId : messageIds)
                if (!info.getMessageIds().contains(messageId))
                    info.insertMessageIds(messageIds);
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
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAntiRecall())
                    {
                        String text = Language.resolve(HostApplicationInfo.getApplication().getResources().getConfiguration().locale, "antirecall.message.deleted");
                        MessageObject messageObject = new MessageObject(param.args[0]);
                        TLRPC.Message owner = messageObject.getMessageOwner();
                        int id = owner.getID();
                        long channel_id = -owner.getPeerID().getChannelID();
                        if (messageIsDeleted(channel_id, id) != null)
                        {
                            if (getCurrentTimeStringClassName(param.thisObject).equals("SpannableStringBuilder"))
                            {
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
                            }
                            else
                            {
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
        }
        else
        {
            Utils.log("Not found ChatMessageCell, " + Utils.issue);
        }
    }

    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), lpparam.classLoader);
        if (messagesController != null) {
            //HookUtils.findAndHookMethod(messagesController, AutomationResolver.resolve("MessagesController", "markDialogMessageAsDeleted", AutomationResolver.ResolverType.Method), XC_MethodReplacement.returnConstant(null));

            //HookUtils.findAndHookMethod(messagesController, AutomationResolver.resolve("MessagesController", "deleteMessages", AutomationResolver.ResolverType.Method), XC_MethodReplacement.returnConstant(null));

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
                                        insertNeedProcessDeletedMessage(-channelMessages.getChannelID(), new CopyOnWriteArrayList<>(channelMessages.getMessages()));
                                    }

                                    if (item.getClass().equals(TL_updateDeleteMessages))
                                        insertNeedProcessDeletedMessage(DeletedMessageInfo.NOT_CHANNEL, new CopyOnWriteArrayList<>(new TLRPC.TL_updateDeleteMessages(item).getMessages()));

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

    public static void initNotification(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> messagesStorage = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.MessagesStorage"));
        Class<?> notificationCenter = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationCenter"));
        Class<?> notificationsController = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationsController"));

        List<Method> markMessagesAsDeletedMethods = new ArrayList<>();
        for (Method method : messagesStorage.getDeclaredMethods()) {
            if (method.getName().equals(AutomationResolver.resolve("MessagesStorage", "markMessagesAsDeleted", AutomationResolver.ResolverType.Method))) {
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
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAntiRecall() && param.args[1] instanceof ArrayList)
                    {
                        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(Utils.castList(param.args[1], Integer.class));
                        if (list.isEmpty())
                            return;
                        long channel_id = (long) param.args[0];
                        if (channel_id > 0)
                            channel_id = 0;
                        //list.clear();
                        CopyOnWriteArrayList<Integer> deletedMessages = new CopyOnWriteArrayList<>();
                        for (Integer integer : list) {
                            DeletedMessageInfo info = AntiRecall.findInNeedProcess(channel_id, integer);
                            if (messageIsDeleted(channel_id, integer) == null || info != null)
                            {
                                list.remove(integer);
                                deletedMessages.add(integer);
                            }
                            else if (messageIsDeleted(channel_id, integer) != null)
                            {
                                removeDeletedMessage(channel_id, integer);
                            }
                        }
                        //list.removeIf(i -> (AntiRecall.findInNeedProcess(channel_id, i) || AntiRecall.messageIsDeleted(channel_id, i)));
                        param.args[1] = new ArrayList<>(list);
                        insertDeletedMessage(channel_id, deletedMessages);
                        needProcessing.clear();
                        //needProcessing.forEach(i -> AntiRecall.insertDeletedMessage(channel_id, i));
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
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args[2] instanceof ArrayList)
                    {
                        long channelID = -((long) param.args[1]);
                        if (channelID > 0)
                            channelID = 0;
                        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(Utils.castList(param.args[2], Integer.class));
                        if (!list.isEmpty())
                            for (Integer integer : list)
                            {
                                DeletedMessageInfo info = messageIsDeleted2(channelID, integer);
                                if (info == null)
                                {
                                    param.setResult(null);
                                    DeletedMessageInfo info2 = messageIsDeleted(channelID, integer);
                                    if (info2 == null)
                                    {
                                        list.remove(integer);
                                        insertNeedProcessDeletedMessage(channelID, integer);
                                    }
                                    else
                                    {
                                        deletedMessages2Ids.add(info2);
                                        insertNeedProcessDeletedMessage(channelID, integer);
                                    }
                                }
                                else
                                {
                                    deletedMessagesIds.add(info);
                                    deletedMessages2Ids.remove(info);
                                }
                            }
                        param.args[2] = new ArrayList<>(list);
                    }
                }
            });
        }

        int messagesDeletedValue = (int) XposedHelpers.getStaticObjectField(notificationCenter, AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field));

        HookUtils.findAndHookMethod(notificationCenter, AutomationResolver.resolve("NotificationCenter", "postNotificationName", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (Configs.isAntiRecall() && ((int) param.args[0]) == messagesDeletedValue) {
                    param.setResult(null);
                    /*
                    Object[] args = (Object[]) param.args[1];
                    if (args[0] == null || args[1] == null || args[2] == null)
                        return;
                    if (args[0] instanceof ArrayList && args[1].getClass().isPrimitive() && args[2].getClass().isPrimitive())
                    {
                        param.setResult(null);
                        long dialogID = -((long) args[1]);
                        if (dialogID > 0)
                            dialogID = 0;
                        insertNeedProcessDeletedMessage(dialogID, Utils.castList(args[0], Integer.class));
                    }

                     */
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
