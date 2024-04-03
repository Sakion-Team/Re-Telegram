package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class FakePremium {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        //XposedHelpers.findAndHookMethod(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), lpparam.classLoader, AutomationResolver.resolve("isPremium"), XC_MethodReplacement.returnConstant(true));
    }
}
