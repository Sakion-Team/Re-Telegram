package nep.timeline.re_telegram.viruals;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.MethodUtils;

public class MessagesController {
    private final Object instance;

    public MessagesController(Object instance)
    {
        this.instance = instance;
    }

    public static MessagesController getInstance() {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), Utils.globalLoadPackageParam.classLoader);
        String getInstanceMethod = AutomationResolver.resolve("MessagesController", "getInstance", AutomationResolver.ResolverType.Method);
        return new MessagesController(MethodUtils.invokeMethodOfClass(messagesController, getInstanceMethod, UserConfig.getSelectedAccount()));
    }
}
