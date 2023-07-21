package nep.timeline.re_telegram.features;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.HookInit;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.application.HostApplicationInfo;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.structs.DeletedMessageInfo;
import nep.timeline.re_telegram.virtuals.MessageObject;
import nep.timeline.re_telegram.virtuals.OfficialChatMessageCell;
import nep.timeline.re_telegram.virtuals.TLRPC;
import nep.timeline.re_telegram.virtuals.Theme;
import nep.timeline.re_telegram.virtuals.UserConfig;
import nep.timeline.re_telegram.virtuals.nekogram.NekoChatMessageCell;

public class AntiRecall {
    private static final List<DeletedMessageInfo> deletedMessagesIds = new ArrayList<>();
    private static final List<DeletedMessageInfo> needProcessing = new ArrayList<>();

    public static List<DeletedMessageInfo> getDeletedMessagesIds() {
        return deletedMessagesIds;
    }

    public static boolean messageIsDeleted(long channelID, int messageId) {
        boolean deleted = false;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
            {
                deleted = true;
                break;
            }
        }
        return deleted; // deletedMessagesIds.contains(messageId);
    }

    public static boolean findInNeedProcess(long channelID, int messageId) {
        boolean deleted = false;
        for (DeletedMessageInfo deletedMessagesId : needProcessing) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getChannelID() == channelID && deletedMessagesId.getMessageIds().contains(messageId))
            {
                deleted = true;
                break;
            }
        }
        return deleted;
    }

    public static void insertDeletedMessage(long channelID, ArrayList<Integer> messageIds) {
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

    public static void removeDeletedMessage(long channelID, DeletedMessageInfo messageInfo) {
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
            for (Integer messageId : messageInfo.getMessageIds())
                if (info.getMessageIds().contains(messageId))
                    info.removeMessageId(messageId);
            Utils.saveDeletedMessages();
        }
    }

    public static void insertNeedProcessDeletedMessage(long channelID, ArrayList<Integer> messageIds) {
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

    public static void insertDeletedMessageFromSaveFile(int selectedAccount, long channelID, ArrayList<Integer> messageIds) {
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

    public static void initUI(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> chatMessageCell = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Cells.ChatMessageCell"), lpparam.classLoader);

        if (chatMessageCell != null) {
            HookUtils.findAndHookMethod(chatMessageCell, AutomationResolver.resolve("ChatMessageCell", "measureTime", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAntiRecall())
                    {
                        String text = "deleted";
                        switch (HostApplicationInfo.getApplication().getResources().getConfiguration().locale.getDisplayLanguage())
                        {
                            case "\u65e5\u672c\u8a9e":
                            case "\u4e2d\u6587":
                                text = "\u5df2\u64a4\u56de";
                                break;
                        }

                        MessageObject messageObject = new MessageObject(param.args[0]);
                        TLRPC.Message owner = messageObject.getMessageOwner();
                        int id = owner.getID();
                        long channel_id = -owner.getPeerID().getChannelID();
                        if (messageIsDeleted(channel_id, id))
                        {
                            if (ClientChecker.isNekogram() || ClientChecker.isYukigram())
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
                            ArrayList<Object> updates = Utils.castList(param.args[0], Object.class);
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
                                        insertNeedProcessDeletedMessage(-channelMessages.getChannelID(), channelMessages.getMessages());
                                    }

                                    if (item.getClass().equals(TL_updateDeleteMessages))
                                        insertNeedProcessDeletedMessage(DeletedMessageInfo.NOT_CHANNEL, new TLRPC.TL_updateDeleteMessages(item).getMessages());

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

        ArrayList<Method> markMessagesAsDeletedMethods = new ArrayList<>();
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
                        ArrayList<Integer> list = Utils.castList(param.args[1], Integer.class);
                        long channel_id = (long) param.args[0];
                        //list.clear();
                        ArrayList<Integer> deletedMessages = new ArrayList<>();
                        for (Integer integer : list) {
                            if (!messageIsDeleted(channel_id, integer) || AntiRecall.findInNeedProcess(channel_id, integer))
                            {
                                list.remove(integer);
                                deletedMessages.add(integer);
                            }
                            else if (messageIsDeleted(channel_id, integer))
                            {
                                removeDeletedMessage(channel_id, integer);
                            }
                        }
                        //list.removeIf(i -> (AntiRecall.findInNeedProcess(channel_id, i) || AntiRecall.messageIsDeleted(channel_id, i)));
                        param.args[1] = list;
                        insertDeletedMessage(channel_id, deletedMessages);
                        //needProcessing.forEach(i -> AntiRecall.insertDeletedMessage(channel_id, i));
                        needProcessing.clear();
                    }
                }
            });
        }

        ArrayList<Method> updateDialogsWithDeletedMessagesMethods = new ArrayList<>();
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
                    //param.setResult(null);
                    if (param.args[2] instanceof ArrayList)
                    {
                        insertNeedProcessDeletedMessage((long) param.args[1], Utils.castList(param.args[2], Integer.class));
                    }
                }
            });
        }

        int messagesDeletedValue = (int) XposedHelpers.getStaticObjectField(notificationCenter, AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field));

        Method postNotificationName = notificationCenter.getDeclaredMethod(AutomationResolver.resolve("NotificationCenter", "postNotificationName", AutomationResolver.ResolverType.Method), int.class, Object[].class);

        XposedBridge.hookMethod(postNotificationName, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (Configs.isAntiRecall() && ((int) param.args[0]) == messagesDeletedValue) {
                    Object[] args = (Object[]) param.args[1];
                    long dialogID = (long) args[1];
                    param.setResult(null);
                    if (args[0] instanceof ArrayList<?>)
                    {
                        insertNeedProcessDeletedMessage(dialogID, Utils.castList(args[0], Integer.class));
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
            XposedBridge.hookMethod(removeDeletedMessagesFromNotifications, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAntiRecall())
                        param.setResult(null);
                }
            });
    }
}
