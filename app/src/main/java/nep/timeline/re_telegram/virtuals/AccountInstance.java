package nep.timeline.re_telegram.virtuals;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.MethodUtils;

public class AccountInstance {
    private final Object instance;

    public AccountInstance(Object instance)
    {
        this.instance = instance;
    }

    public static AccountInstance getInstance(ClassLoader classLoader) {
        Class<?> accountInstance = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.AccountInstance"), classLoader);
        String getInstanceMethod = AutomationResolver.resolve("AccountInstance", "getInstance", AutomationResolver.ResolverType.Method);
        return new AccountInstance(MethodUtils.invokeMethodOfClass(accountInstance, getInstanceMethod, UserConfig.getSelectedAccount()));
    }

    public MessagesController getMessagesController() {
        String method = AutomationResolver.resolve("AccountInstance", "getMessagesController", AutomationResolver.ResolverType.Method);
        return new MessagesController(MethodUtils.invokeMethodOfClass(this.instance, method));
    }

    public MessagesStorage getMessagesStorage() {
        String method = AutomationResolver.resolve("AccountInstance", "getMessagesStorage", AutomationResolver.ResolverType.Method);
        return new MessagesStorage(MethodUtils.invokeMethodOfClass(this.instance, method));
    }
}
