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
        classList.add(new ClassInfo("org.telegram.messenger.ApplicationLoader", "i7"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationsController", "Lr0"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationCenter", "gq0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesController", "Wl0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesStorage", "en0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessageObject", "Dj0"));
        classList.add(new ClassInfo("org.telegram.messenger.UserConfig", "D81"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$Message", "YL0"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$Peer", "oM0"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteMessages", "Ja1"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteChannelMessages", "Ia1"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteScheduledMessages", "Ka1"));
        classList.add(new ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "org.telegram.ui.Cells.r"));
        classList.add(new ClassInfo("org.telegram.ui.ActionBar.Theme", "F21"));
        //classList.add(new ClassInfo("org.telegram.ui.ChatPullingDownDrawable", "org.telegram.ui.m3"));
        classList.add(new ClassInfo("org.telegram.ui.ChatActivity", "org.telegram.ui.Z2"));
        classList.add(new ClassInfo("org.telegram.ui.DialogsActivity", "org.telegram.ui.Y3"));
        classList.add(new ClassInfo("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback", "org.telegram.ui.q1"));
        classList.add(new ClassInfo("org.telegram.messenger.AndroidUtilities", "L5"));

        fieldList.add(new FieldInfo("MessageObject", "messageOwner", "j"));
        fieldList.add(new FieldInfo("UserConfig", "selectedAccount", "o0"));
        fieldList.add(new FieldInfo("Theme", "chat_timePaint", "C2"));
        fieldList.add(new FieldInfo("NotificationCenter", "messagesDeleted", "w"));
        fieldList.add(new FieldInfo("AndroidUtilities", "typefaceCache", "a"));

        //methodList.add(new MethodInfo("ApplicationLoader", "onCreate", "n"));
        methodList.add(new MethodInfo("NotificationCenter", "postNotificationName", "i"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeleted", "D0"));
        methodList.add(new MethodInfo("MessagesStorage", "updateDialogsWithDeletedMessages", "F1"));
        methodList.add(new MethodInfo("MessageObject", "updateMessageText", "K3"));
        methodList.add(new MethodInfo("MessagesController", "isChatNoForwards", "m1"));
        methodList.add(new MethodInfo("MessagesController", "markDialogMessageAsDeleted", "S1"));
        methodList.add(new MethodInfo("MessagesController", "deleteMessages", "Q"));
        methodList.add(new MethodInfo("MessagesController", "getInstance", "J0"));
        methodList.add(new MethodInfo("MessageObject", "canForwardMessage", "q"));
        methodList.add(new MethodInfo("ChatMessageCell", "measureTime", "S4"));
        methodList.add(new MethodInfo("UserConfig", "getInstance", "g"));
        methodList.add(new MethodInfo("UserConfig", "isPremium", "o"));
        methodList.add(new MethodInfo("NotificationsController", "removeNotificationsForDialog", "H"));
        methodList.add(new MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "G"));
        //methodList.add(new MethodInfo("NotificationsController", "loadRoundAvatar", "w"));
        methodList.add(new MethodInfo("ChatActivity", "addSponsoredMessages", "Zf"));
        methodList.add(new MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "Sh"));
        methodList.add(new MethodInfo("DialogsActivity", "onDefaultTabMoved", "B0"));
        methodList.add(new MethodInfo("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", "o"));
        methodList.add(new MethodInfo("AndroidUtilities", "getTypeface", "L0"));
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
