package nep.timeline.re_telegram.virtuals;

public class UserConfig {
    public static int getSelectedAccount() {
        return 0;
        //Class<?> userConfig = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.UserConfig"), Utils.globalLoadPackageParam.classLoader);
        //String selectedAccountField = AutomationResolver.resolve("UserConfig", "selectedAccount", AutomationResolver.ResolverType.Field);
        //return FieldUtils.getFieldIntOfClass(null, userConfig, selectedAccountField);
    }
}
