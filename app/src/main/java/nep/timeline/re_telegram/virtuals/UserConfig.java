package nep.timeline.re_telegram.virtuals;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class UserConfig {
    public static int getSelectedAccount() {
        return 0;
        //Class<?> userConfig = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), Utils.globalLoadPackageParam.classLoader);
        //String selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field);
        //return FieldUtils.getFieldIntOfClass(null, userConfig, selectedAccountField);
    }

    public static int getSelectedAccount(ClassLoader classLoader) {
        Class<?> userConfig = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), classLoader);
        String selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field);
        return XposedHelpers.getStaticIntField(userConfig, selectedAccountField);
    }
}
