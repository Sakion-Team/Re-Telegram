package nep.timeline.re_telegram.features;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class ProhibitChannelSwitching {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class<?> chatPullingDownDrawable = lpparam.classLoader.loadClass(AutomationResolver.resolve("org.telegram.ui.ChatPullingDownDrawable"));
        for (Method method : chatPullingDownDrawable.getDeclaredMethods())
        {
            if (method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "getNextUnreadDialog", AutomationResolver.ResolverType.Method))
                    || method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "drawBottomPanel", AutomationResolver.ResolverType.Method))
                    || method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "draw", AutomationResolver.ResolverType.Method)))
            {
                if (Configs.isProhibitChannelSwitching())
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (Configs.isProhibitChannelSwitching())
                                param.setResult(null);
                        }
                    });
            }

            if (method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "showBottomPanel", AutomationResolver.ResolverType.Method)))
            {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Configs.isProhibitChannelSwitching())
                            param.args[0] = false;
                    }
                });
            }

            if (!ClientChecker.check(ClientChecker.ClientType.Nekogram) && method.getName().equals(AutomationResolver.resolve("ChatPullingDownDrawable", "needDrawBottomPanel", AutomationResolver.ResolverType.Method)))
            {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Configs.isProhibitChannelSwitching())
                            param.setResult(false);
                    }
                });
            }
        }
    }
}
