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
        classList.add(new ClassInfo("org.telegram.messenger.NotificationsController", "org.telegram.messenger.J"));
        classList.add(new ClassInfo("org.telegram.messenger.NotificationCenter", "org.telegram.messenger.I"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesController", "org.telegram.messenger.G"));
        classList.add(new ClassInfo("org.telegram.messenger.MessagesStorage", "org.telegram.messenger.H"));
        classList.add(new ClassInfo("org.telegram.messenger.MessageObject", "org.telegram.messenger.E"));
        classList.add(new ClassInfo("org.telegram.messenger.UserConfig", "org.telegram.messenger.X"));
        classList.add(new ClassInfo("org.telegram.ui.Cells.ChatMessageCell", "r50"));
        classList.add(new ClassInfo("org.telegram.ui.ActionBar.Theme", "org.telegram.ui.ActionBar.r"));
        classList.add(new ClassInfo("org.telegram.ui.ChatActivity", "org.telegram.ui.o"));
        classList.add(new ClassInfo("org.telegram.ui.DialogsActivity", "org.telegram.ui.G$i")); // LimitReachedReorderFolder
        classList.add(new ClassInfo("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback", "org.telegram.ui.Components.f0$l"));
        classList.add(new ClassInfo("org.telegram.messenger.AndroidUtilities", "org.telegram.messenger.a"));

        //fieldList.add(new FieldInfo("MessageObject", "messageOwner", "j"));
        fieldList.add(new FieldInfo("UserConfig", "selectedAccount", "a0"));
        fieldList.add(new FieldInfo("TLRPC$Peer", "channel_id", "c"));
        fieldList.add(new FieldInfo("TLRPC$Message", "id", "a"));
        fieldList.add(new FieldInfo("TLRPC$Message", "flags", "k"));
        fieldList.add(new FieldInfo("TLRPC$Message", "peer_id", "d"));
        fieldList.add(new FieldInfo("TLRPC$TL_updateDeleteMessages", "messages", "a"));
        fieldList.add(new FieldInfo("TLRPC$TL_updateDeleteChannelMessages", "channel_id", "a"));
        fieldList.add(new FieldInfo("TLRPC$TL_updateDeleteChannelMessages", "messages", "b"));
        fieldList.add(new FieldInfo("Theme", "chat_timePaint", "K2"));
        fieldList.add(new FieldInfo("MessagesController", "dialogMessagesByIds", "G"));
        fieldList.add(new FieldInfo("MessagesController", "dialogMessage", "D"));
        fieldList.add(new FieldInfo("NotificationCenter", "messagesDeleted", "v"));
        fieldList.add(new FieldInfo("AndroidUtilities", "typefaceCache", "b"));

        methodList.add(new MethodInfo("NotificationCenter", "postNotificationName", "L"));
        methodList.add(new MethodInfo("MessagesStorage", "markMessagesAsDeletedInternal", "gb"));
        methodList.add(new MethodInfo("MessagesStorage", "updateDialogsWithDeletedMessagesInternal", "ca"));
        methodList.add(new MethodInfo("MessagesStorage", "getDatabase", "a5"));
        methodList.add(new MethodInfo("MessageObject", "updateMessageText", "B6"));
        methodList.add(new MethodInfo("MessageObject", "canForwardMessage", "K"));
        methodList.add(new MethodInfo("MessagesController", "isChatNoForwards", "xb"));
        methodList.add(new MethodInfo("MessagesController", "markDialogMessageAsDeleted", "ll"));
        methodList.add(new MethodInfo("MessagesController", "deleteMessages", "U8"));
        methodList.add(new MethodInfo("MessagesController", "getSponsoredMessages", "Qa"));
        methodList.add(new MethodInfo("ChatMessageCell", "measureTime", "Z6"));
        methodList.add(new MethodInfo("UserConfig", "getInstance", "r"));
        //methodList.add(new MethodInfo("UserConfig", "isPremium", "B"));
        methodList.add(new MethodInfo("NotificationsController", "removeNotificationsForDialog", "c2"));
        methodList.add(new MethodInfo("NotificationsController", "removeDeletedMessagesFromNotifications", "b2"));
        methodList.add(new MethodInfo("ChatActivity", "addSponsoredMessages", "Ap"));
        methodList.add(new MethodInfo("ChatActivity", "hasSelectedNoforwardsMessage", "As"));
        methodList.add(new MethodInfo("DialogsActivity", "onDefaultTabMoved", "D"));
        methodList.add(new MethodInfo("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", "D"));
        methodList.add(new MethodInfo("AndroidUtilities", "getTypeface", "X1"));
        methodList.add(new MethodInfo("SQLiteDatabase", "queryFinalized", "h"));
        methodList.add(new MethodInfo("SQLiteDatabase", "executeFast", "e"));
        methodList.add(new MethodInfo("SQLiteCursor", "next", "j"));
        methodList.add(new MethodInfo("SQLiteCursor", "byteBufferValue", "b"));
        methodList.add(new MethodInfo("SQLiteCursor", "intValue", "g"));
        methodList.add(new MethodInfo("SQLiteCursor", "longValue", "i"));
        methodList.add(new MethodInfo("SQLiteCursor", "dispose", "d"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "dispose", "h"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "requery", "l"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "bindByteBuffer", "a"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "bindLong", "d"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "bindInteger", "c"));
        methodList.add(new MethodInfo("SQLitePreparedStatement", "step", "m"));
        methodList.add(new MethodInfo("LongSparseArray", "get", "h"));
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
