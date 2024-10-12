package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class FakePremium {
    public static void init(ClassLoader classLoader)
    {
        XposedHelpers.findAndHookMethod(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), classLoader, AutomationResolver.resolve("isPremium"), XC_MethodReplacement.returnConstant(true));
    }
}
