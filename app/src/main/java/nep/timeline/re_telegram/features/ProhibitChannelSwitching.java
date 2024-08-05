package nep.timeline.re_telegram.features;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class ProhibitChannelSwitching {
    public static void init(ClassLoader classLoader) {
        Class<?> chatPullingDownDrawable = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ChatPullingDownDrawable"), classLoader);
        for (Method method : chatPullingDownDrawable.getDeclaredMethods())
        {
            if (method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "getNextUnreadDialog", AutomationResolver.ResolverType.Method))
                    || method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "drawBottomPanel", AutomationResolver.ResolverType.Method))
                    || method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "draw", AutomationResolver.ResolverType.Method)))
            {
                XposedBridge.hookMethod(method, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isProhibitChannelSwitching())
                            param.setResult(null);
                    }
                });
            }

            if (method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "showBottomPanel", AutomationResolver.ResolverType.Method)))
            {
                XposedBridge.hookMethod(method, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isProhibitChannelSwitching())
                            param.args[0] = false;
                    }
                });
            }

            if (!ClientChecker.check(ClientChecker.ClientType.Nekogram) && method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "needDrawBottomPanel", AutomationResolver.ResolverType.Method)))
            {
                XposedBridge.hookMethod(method, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isProhibitChannelSwitching())
                            param.setResult(false);
                    }
                });
            }
        }
    }
}
