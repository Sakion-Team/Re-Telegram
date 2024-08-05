package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class NoSponsoredMessages {
    public static void init(ClassLoader classLoader)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), classLoader);
        Class<?> chatActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ChatActivity"), classLoader);

        if (ClientChecker.check(ClientChecker.ClientType.Yukigram)) {
            XposedHelpers.findAndHookMethod(chatActivity, AutomationResolver.resolve("ChatActivity", "addSponsoredMessages", AutomationResolver.ResolverType.Method), boolean.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isNoSponsoredMessages())
                        param.setResult(null);
                }
            });
        } else {
            XposedHelpers.findAndHookMethod(chatActivity, AutomationResolver.resolve("ChatActivity", "addSponsoredMessages", AutomationResolver.ResolverType.Method), boolean.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isNoSponsoredMessages())
                        param.setResult(null);
                }
            });

            XposedHelpers.findAndHookMethod(messagesController, AutomationResolver.resolve("MessagesController", "getSponsoredMessages", AutomationResolver.ResolverType.Method), long.class, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isNoSponsoredMessages())
                        param.setResult(null);
                }
            });
            /*List<Method> methods = new ArrayList<>();

            for (Method method : messagesController.getDeclaredMethods()) {
                if (method.getName().contains("SponsoredMessages"))
                    methods.add(method);
            }

            for (Method method : chatActivity.getDeclaredMethods()) {
                if (method.getName().contains("SponsoredMessages"))
                    methods.add(method);
            }

            for (Method method : methods) {
                XposedBridge.hookMethod(method, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isNoSponsoredMessages())
                            param.setResult(null);
                    }
                });
            }*/
        }
    }
}
