package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class UnlockedNoPremiumAccountsLimit {
    public static void init(ClassLoader classLoader)
    {
        if (Configs.isUnlockedNoPremiumAccountsLimit())
            XposedHelpers.findAndHookMethod(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), classLoader, AutomationResolver.resolve("hasPremiumOnAccounts"), XC_MethodReplacement.returnConstant(true));
    }
}
