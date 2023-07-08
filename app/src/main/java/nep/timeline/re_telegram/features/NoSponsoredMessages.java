package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class NoSponsoredMessages {
    public static void init(Class<?> messagesController)
    {
        String getSponsoredMessagesMethod = AutomationResolver.resolve("MessagesController", "getSponsoredMessages", AutomationResolver.ResolverType.Method);
        XposedHelpers.findAndHookMethod(messagesController, getSponsoredMessagesMethod, long.class, XC_MethodReplacement.returnConstant(null));
    }
}
