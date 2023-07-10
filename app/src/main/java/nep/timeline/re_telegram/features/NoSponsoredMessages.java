package nep.timeline.re_telegram.features;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class NoSponsoredMessages {
    public static void init(XC_LoadPackage.LoadPackageParam loadPackageParam)
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), loadPackageParam.classLoader);

        String getSponsoredMessagesMethod = AutomationResolver.resolve("MessagesController", "getSponsoredMessages", AutomationResolver.ResolverType.Method);
        boolean find = false;
        for (Method declaredMethod : messagesController.getDeclaredMethods()) {
            if (declaredMethod.getName().equals(getSponsoredMessagesMethod))
            {
                find = true;
                break;
            }
        }
        if (find)
            XposedHelpers.findAndHookMethod(messagesController, getSponsoredMessagesMethod, long.class, XC_MethodReplacement.returnConstant(null));
        else
        {
            Class<?> chatActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ChatActivity"), loadPackageParam.classLoader);
            HookUtils.findAndHookMethod(chatActivity, AutomationResolver.resolve("ChatActivity", "addSponsoredMessages", AutomationResolver.ResolverType.Method), XC_MethodReplacement.returnConstant(null));
        }
    }
}
