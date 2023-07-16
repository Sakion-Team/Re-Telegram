package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class AntiAntiForward {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), lpparam.classLoader);
        if (messagesController != null)
        {
            String isChatNoForwardsMethod = AutomationResolver.resolve("MessagesController", "isChatNoForwards", AutomationResolver.ResolverType.Method);
            HookUtils.findAndHookAllMethod(messagesController, isChatNoForwardsMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAntiAntiForward())
                        param.setResult(false);
                }
            });
        }
        else
        {
            Utils.log("Not found MessagesController, " + Utils.issue);
        }

        if (!ClientChecker.isYukigram())
        {
            Class<?> messageObject = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessageObject"), lpparam.classLoader);
            if (messageObject != null)
            {
                String canForwardMessageMethod = AutomationResolver.resolve("MessageObject", "canForwardMessage", AutomationResolver.ResolverType.Method);
                XposedHelpers.findAndHookMethod(messageObject, canForwardMessageMethod, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Configs.isAntiAntiForward())
                            param.setResult(false);
                    }
                });
            }
            else
            {
                Utils.log("Not found MessageObject, " + Utils.issue);
            }
        }
    }
}
