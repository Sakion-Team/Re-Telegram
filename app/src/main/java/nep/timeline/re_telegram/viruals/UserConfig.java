package nep.timeline.re_telegram.viruals;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.FieldUtils;

public class UserConfig {
    public static int getSelectedAccount() {
        Class<?> userConfig = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), Utils.globalLoadPackageParam.classLoader);
        String selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field);
        return FieldUtils.getFieldIntOfClass(null, userConfig, selectedAccountField);
    }
}
