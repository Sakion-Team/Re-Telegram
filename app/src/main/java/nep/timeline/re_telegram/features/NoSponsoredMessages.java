package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class NoSponsoredMessages {
    public static void init(XC_LoadPackage.LoadPackageParam loadPackageParam)
    {
        Class<?> chatActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ChatActivity"), loadPackageParam.classLoader);
        HookUtils.findAndHookMethod(chatActivity, AutomationResolver.resolve("ChatActivity", "addSponsoredMessages", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (Configs.isNoSponsoredMessages())
                    param.setResult(null);
            }
        });
    }
}
