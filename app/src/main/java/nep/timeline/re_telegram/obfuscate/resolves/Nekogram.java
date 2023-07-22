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
        classList.add(new ClassInfo("org.telegram.messenger.NotificationsController", "Uj0"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationCenter", "ui0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesStorage", "yf0"));
        classList.add(new ClassInfo("org.telegram.messenger.MessageObject", "bc0"));
        classList.add(new ClassInfo("org.telegram.messenger.UserConfig", "sm1"));
        classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$Message", "YL0"));
        classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$Peer", "oM0"));
        classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteMessages", "Ja1"));
        classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteChannelMessages", "Ia1"));
        //classList.add(new ClassInfo("org.telegram.tgnet.TLRPC$TL_updateDeleteScheduledMessages", "Ka1"));
        classList.add(new ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "org.telegram.ui.Cells.r"));
        classList.add(new ClassInfo("org.telegram.ui.ActionBar.Theme", "gh1"));
        classList.add(new ClassInfo("org.telegram.ui.ChatPullingDownDrawable", "org.telegram.ui.b3"));
        classList.add(new ClassInfo("org.telegram.ui.DialogsActivity$6", "org.telegram.ui.Q3"));
        classList.add(new ClassInfo("org.telegram.ui.Components.FilterTabsView$TouchHelperCallback", "org.telegram.ui.l1"));

        fieldList.add(new FieldInfo("MessageObject", "messageOwner", "a"));
        fieldList.add(new FieldInfo("UserConfig", "selectedAccount", "o"));
        fieldList.add(new FieldInfo("Theme", "chat_timePaint", "J"));
        fieldList.add(new FieldInfo("NotificationCenter", "messagesDeleted", "i"));

        methodList.add(new MethodInfo("ApplicationLoader", "onCreate", "n"));
        methodList.add(new MethodInfo("NotificationsController", "removeNotificationsForDialog", "E"));
        methodList.add(new MethodInfo("NotificationCenter", "postNotificationName", "i"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeleted", "z0"));
        methodList.add(new MethodInfo("MessagesStorage", "updateDialogsWithDeletedMessages", "B1"));
        methodList.add(new MethodInfo("MessageObject", "updateMessageText", "y3"));
        methodList.add(new MethodInfo("MessagesController", "isChatNoForwards", "i1"));
        methodList.add(new MethodInfo("MessagesController", "markDialogMessageAsDeleted", "N1"));
        methodList.add(new MethodInfo("MessagesController", "deleteMessages", "P"));
        methodList.add(new MethodInfo("MessageObject", "canForwardMessage", "n"));
        methodList.add(new MethodInfo("MessagesController", "getInstance", "I0"));
        methodList.add(new MethodInfo("ChatMessageCell", "measureTime", "W4"));
        methodList.add(new MethodInfo("UserConfig", "getInstance", "g"));
        methodList.add(new MethodInfo("UserConfig", "isPremium", "o"));
        methodList.add(new MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "D"));
        methodList.add(new MethodInfo("NotificationsController", "loadRoundAvatar", "w"));
        methodList.add(new MethodInfo("ChatPullingDownDrawable", "getNextUnreadDialog", "g"));
        methodList.add(new MethodInfo("ChatPullingDownDrawable", "drawBottomPanel", "f"));
        methodList.add(new MethodInfo("ChatPullingDownDrawable", "draw", "e"));
        methodList.add(new MethodInfo("ChatPullingDownDrawable", "showBottomPanel", "l"));
        methodList.add(new MethodInfo("DialogsActivity$6", "onDefaultTabMoved", "B0"));
        methodList.add(new MethodInfo("FilterTabsView$TouchHelperCallback", "lambda$new$0", "o"));
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
