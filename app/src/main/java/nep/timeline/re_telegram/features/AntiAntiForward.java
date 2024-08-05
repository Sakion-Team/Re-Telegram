package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class AntiAntiForward {
    public static void init(ClassLoader classLoader)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), classLoader);
        if (messagesController != null)
        {
            String isChatNoForwardsMethod = AutomationResolver.resolve("MessagesController", "isChatNoForwards", AutomationResolver.ResolverType.Method);
            HookUtils.findAndHookAllMethod(messagesController, isChatNoForwardsMethod, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isAntiAntiForward())
                        param.setResult(false);
                }
            });
        }
        else
        {
            Utils.log("Not found MessagesController, " + Utils.issue);
        }

        Class<?> chatActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ChatActivity"), classLoader);
        if (chatActivity != null)
        {
            String hasSelectedNoforwardsMessageMethod = AutomationResolver.resolve("ChatActivity", "hasSelectedNoforwardsMessage", AutomationResolver.ResolverType.Method);
            XposedHelpers.findAndHookMethod(chatActivity, hasSelectedNoforwardsMessageMethod, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isAntiAntiForward())
                        param.setResult(false);
                }
            });
        }
        else
        {
            Utils.log("Not found ChatActivity, " + Utils.issue);
        }

        Class<?> messageObject = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessageObject"), classLoader);
        if (messageObject != null)
        {
            String canForwardMessageMethod = AutomationResolver.resolve("MessageObject", "canForwardMessage", AutomationResolver.ResolverType.Method);
            XposedHelpers.findAndHookMethod(messageObject, canForwardMessageMethod, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isAntiAntiForward())
                        param.setResult(true);
                }
            });
        }
        else
        {
            Utils.log("Not found MessageObject, " + Utils.issue);
        }
    }
}