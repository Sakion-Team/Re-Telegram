package nep.timeline.re_telegram.obfuscate.resolves;

import java.util.ArrayList;
import java.util.List;

import nep.timeline.re_telegram.obfuscate.struct.ClassInfo;
import nep.timeline.re_telegram.obfuscate.struct.FieldInfo;
import nep.timeline.re_telegram.obfuscate.struct.MethodInfo;

public class Nekogram {
    private static final List<ClassInfo> classList = new ArrayList<>();
    private static final List<FieldInfo> fieldList = new ArrayList<>();
    private static final List<MethodInfo> methodList = new ArrayList<>();

    static {
        classList.add(new ClassInfo("org.telegram.messenger.ApplicationLoader", "org.telegram.messenger.b"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationsController", "org.telegram.messenger.I"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationCenter", "org.telegram.messenger.H"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesController", "org.telegram.messenger.F"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesStorage", "org.telegram.messenger.G"));
        classList.add(new ClassInfo("org.telegram.messenger.MessageObject", "org.telegram.messenger.D"));
        classList.add(new ClassInfo("org.telegram.messenger.UserConfig", "org.telegram.messenger.V"));
        classList.add(new ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "uV"));
        classList.add(new ClassInfo("org.telegram.ui.ActionBar.Theme", "org.telegram.ui.ActionBar.o"));
        classList.add(new ClassInfo("org.telegram.ui.ChatActivity", "org.telegram.ui.n"));
        classList.add(new ClassInfo("org.telegram.ui.DialogsActivity", "org.telegram.ui.F$i")); // LimitReachedReorderFolder
        classList.add(new ClassInfo("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback", "org.telegram.ui.Components.j0$l"));
        classList.add(new ClassInfo("org.telegram.messenger.AndroidUtilities", "org.telegram.messenger.a"));

        //fieldList.add(new FieldInfo("MessageObject", "messageOwner", "j"));
        fieldList.add(new FieldInfo("UserConfig", "selectedAccount", "a0"));
        fieldList.add(new FieldInfo("Theme", "chat_timePaint", "I2"));
        fieldList.add(new FieldInfo("NotificationCenter", "messagesDeleted", "v"));
        fieldList.add(new FieldInfo("AndroidUtilities", "typefaceCache", "a"));

        methodList.add(new MethodInfo("NotificationCenter", "postNotificationName", "E"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeleted", "Sa"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeleted2", "Ta"));
        methodList.add(new MethodInfo("MessagesStorage", "updateDialogsWithDeletedMessages", "Dc"));
        methodList.add(new MethodInfo("MessageObject", "updateMessageText", "V5"));
        methodList.add(new MethodInfo("MessageObject", "canForwardMessage", "G"));
        methodList.add(new MethodInfo("MessagesController", "isChatNoForwards", "La"));
        methodList.add(new MethodInfo("MessagesController", "markDialogMessageAsDeleted", "ck"));
        methodList.add(new MethodInfo("MessagesController", "deleteMessages", "w8"));
        methodList.add(new MethodInfo("ChatMessageCell", "measureTime", "v6"));
        methodList.add(new MethodInfo("UserConfig", "getInstance", "r"));
        methodList.add(new MethodInfo("UserConfig", "isPremium", "B"));
        methodList.add(new MethodInfo("NotificationsController", "removeNotificationsForDialog", "U1"));
        methodList.add(new MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "T1"));
        methodList.add(new MethodInfo("ChatActivity", "addSponsoredMessages", "Gm"));
        methodList.add(new MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "vp"));
        methodList.add(new MethodInfo("DialogsActivity", "onDefaultTabMoved", "D"));
        methodList.add(new MethodInfo("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", "D"));
        methodList.add(new MethodInfo("AndroidUtilities", "getTypeface", "K1"));
    }

    public static class ClassResolver
    {
        public static String resolve(String name) {
            for (ClassInfo info : classList)
                if (info.getOriginal().equals(name))
                    return info.getResolved();

            return null;
        }

        public static boolean has(String name)
        {
            boolean has = false;
            for (ClassInfo info : classList) {
                if (info.getOriginal().equals(name)) {
                    has = true;
                    break;
                }
            }
            return has;
        }
    }

    public static class FieldResolver
    {
        public static String resolve(String className, String name) {
            for (FieldInfo info : fieldList)
                if (info.getClassName().equals(className) && info.getOriginal().equals(name))
                    return info.getResolved();

            return null;
        }

        public static boolean has(String className, String name)
        {
            boolean has = false;
            for (FieldInfo info : fieldList) {
                if (info.getClassName().equals(className) && info.getOriginal().equals(name)) {
                    has = true;
                    break;
                }
            }
            return has;
        }
    }

    public static class MethodResolver
    {
        public static String resolve(String className, String name) {
            for (MethodInfo info : methodList)
                if (info.getClassName().equals(className) && info.getOriginal().equals(name))
                    return info.getResolved();

            return null;
        }

        public static boolean has(String className, String name)
        {
            boolean has = false;
            for (MethodInfo info : methodList) {
                if (info.getClassName().equals(className) && info.getOriginal().equals(name)) {
                    has = true;
                    break;
                }
            }
            return has;
        }
    }
}
