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
        classList.add(new ClassInfo("org.telegram.messenger.ApplicationLoader", "Q8"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationsController", "EI0"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationCenter", "cH0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesController", "dC0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesStorage", "jD0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessageObject", "lz0"));
        classList.add(new ClassInfo("org.telegram.messenger.UserConfig", "zA1"));
        classList.add(new ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "org.telegram.ui.Cells.r"));
        classList.add(new ClassInfo("org.telegram.ui.ActionBar.Theme", "bt1"));
        classList.add(new ClassInfo("org.telegram.ui.ChatActivity", "org.telegram.ui.m3"));
        classList.add(new ClassInfo("org.telegram.ui.DialogsActivity", "org.telegram.ui.n4"));
        classList.add(new ClassInfo("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback", "org.telegram.ui.s1"));
        classList.add(new ClassInfo("org.telegram.messenger.AndroidUtilities", "x7"));

        fieldList.add(new FieldInfo("MessageObject", "messageOwner", "j"));
        fieldList.add(new FieldInfo("UserConfig", "selectedAccount", "G0"));
        fieldList.add(new FieldInfo("Theme", "chat_timePaint", "H2"));
        fieldList.add(new FieldInfo("NotificationCenter", "messagesDeleted", "w"));
        fieldList.add(new FieldInfo("AndroidUtilities", "typefaceCache", "a"));

        methodList.add(new MethodInfo("NotificationCenter", "postNotificationName", "i"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeleted", "G0"));
        methodList.add(new MethodInfo("MessagesStorage", "updateDialogsWithDeletedMessages", "H1"));
        methodList.add(new MethodInfo("MessageObject", "updateMessageText", "a4"));
        methodList.add(new MethodInfo("MessageObject", "canForwardMessage", "r"));
        methodList.add(new MethodInfo("MessagesController", "isChatNoForwards", "r1"));
        methodList.add(new MethodInfo("MessagesController", "markDialogMessageAsDeleted", "V1"));
        methodList.add(new MethodInfo("MessagesController", "deleteMessages", "R"));
        methodList.add(new MethodInfo("MessagesController", "getInstance", "N0"));
        methodList.add(new MethodInfo("ChatMessageCell", "measureTime", "Z4"));
        methodList.add(new MethodInfo("UserConfig", "getInstance", "g"));
        methodList.add(new MethodInfo("UserConfig", "isPremium", "o"));
        methodList.add(new MethodInfo("NotificationsController", "removeNotificationsForDialog", "I"));
        methodList.add(new MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "H"));
        //methodList.add(new MethodInfo("NotificationsController", "loadRoundAvatar", "w"));
        methodList.add(new MethodInfo("ChatActivity", "addSponsoredMessages", "pg"));
        methodList.add(new MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "mi"));
        methodList.add(new MethodInfo("DialogsActivity", "onDefaultTabMoved", "B0"));
        methodList.add(new MethodInfo("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", "o"));
        methodList.add(new MethodInfo("AndroidUtilities", "getTypeface", "N0"));
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
