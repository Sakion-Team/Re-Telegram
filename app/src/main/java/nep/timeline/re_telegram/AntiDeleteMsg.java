package nep.timeline.re_telegram;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.structs.DeletedMessageInfo;
import nep.timeline.re_telegram.viruals.UserConfig;

public class AntiDeleteMsg {
    private static final List<DeletedMessageInfo> deletedMessagesIds = new ArrayList<>();

    public static List<DeletedMessageInfo> getDeletedMessagesIds() {
        return deletedMessagesIds;
    }

    public static boolean messageIsDeleted(int messageId) {
        boolean deleted = false;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount() && deletedMessagesId.getMessageIds().contains(messageId))
            {
                deleted = true;
                break;
            }
        }
        return deleted; // deletedMessagesIds.contains(messageId);
    }

    public static void insertDeletedMessage(ArrayList<Integer> messageIds) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == UserConfig.getSelectedAccount())
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            deletedMessagesIds.add(new DeletedMessageInfo(UserConfig.getSelectedAccount(), messageIds));
        else
        {
            for (Integer messageId : messageIds)
                if (!info.getMessageIds().contains(messageId)) // No duplication
                    info.insertMessageIds(messageIds);
        }
        Utils.saveDeletedMessages();
    }

    public static void insertDeletedMessageFromSaveFile(int selectedAccount, ArrayList<Integer> messageIds) {
        boolean needInit = true;
        DeletedMessageInfo info = null;
        for (DeletedMessageInfo deletedMessagesId : deletedMessagesIds) {
            if (deletedMessagesId.getSelectedAccount() == selectedAccount)
            {
                info = deletedMessagesId;
                needInit = false;
                break;
            }
        }
        if (needInit)
            deletedMessagesIds.add(new DeletedMessageInfo(selectedAccount, messageIds));
        else
        {
            for (Integer messageId : messageIds)
                if (!info.getMessageIds().contains(messageId))
                    info.insertMessageIds(messageIds);
        }
    }

    public static void init() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> messagesStorage = Utils.globalLoadPackageParam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.MessagesStorage"));
        Class<?> notificationCenter = Utils.globalLoadPackageParam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationCenter"));
        Class<?> notificationsController = Utils.globalLoadPackageParam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.NotificationsController"));

        int messagesDeletedValue = (int) XposedHelpers.getStaticObjectField(notificationCenter, AutomationResolver.resolve("NotificationCenter", "messagesDeleted", AutomationResolver.ResolverType.Field));

        Method postNotificationName = notificationCenter.getDeclaredMethod(AutomationResolver.resolve("NotificationCenter", "postNotificationName", AutomationResolver.ResolverType.Method), int.class, Object[].class);

        Method removeDeletedMessagesFromNotifications = null;
        for (Method method : notificationsController.getDeclaredMethods()) {
            if (method.getName().equals(AutomationResolver.resolve("NotificationsController", "removeDeletedMessagesFromNotifications", AutomationResolver.ResolverType.Method))) {
                removeDeletedMessagesFromNotifications = method;
            }
        }

        /*
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
                    param.setResult(null);
                    if (param.args[1] instanceof ArrayList)
                    {
                        insertDeletedMessage(Utils.castList(param.args[1], Integer.class));
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
                    param.setResult(null);
                    if (param.args[2] instanceof ArrayList)
                    {
                        insertDeletedMessage(Utils.castList(param.args[2], Integer.class));
                    }
                }
            });
        }
        */

        XposedBridge.hookMethod(postNotificationName, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (((int) param.args[0]) == messagesDeletedValue) {
                    Object[] args = (Object[]) param.args[1];
                    //long dialogID = (long) args[1];
                    ArrayList<Integer> arrayList = Utils.castList(args[0], Integer.class);
                    param.setResult(null);
                    insertDeletedMessage(arrayList);
                }
            }
        });

        if (removeDeletedMessagesFromNotifications == null) {
            Utils.log("Failed to hook removeDeletedMessagesFromNotifications! Reason: No method found, " + Utils.issue);
            return;
        }

        XposedBridge.hookMethod(removeDeletedMessagesFromNotifications, XC_MethodReplacement.returnConstant(null));
    }
}
