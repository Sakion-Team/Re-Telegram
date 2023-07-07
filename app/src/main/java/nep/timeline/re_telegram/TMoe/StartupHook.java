package nep.timeline.re_telegram.TMoe;

import android.app.Application;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class StartupHook {
    public static final StartupHook INSTANCE = new StartupHook();

    private boolean initialized = false;

    public void doInit(ClassLoader rtLoader) {
        // our minSdk is 21 so there is no need to wait for MultiDex to initialize
        if (initialized)
            return;
        if (rtLoader == null) {
            throw new AssertionError("StartupHook.doInit: rtLoader == null");
        }
        Class<?> applicationClass = null;
        try {
            applicationClass = rtLoader.loadClass(AutomationResolver.resolve("org.telegram.messenger.ApplicationLoader"));
        } catch (ClassNotFoundException ignored) {
        }
        if (applicationClass == null) {
            try {
                applicationClass = rtLoader.loadClass("org.thunderdog.challegram.BaseApplication");
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (applicationClass == null) {
            throw new AssertionError("StartupHook.doInit: unable to find ApplicationLoader");
        }
        XposedHelpers.findAndHookMethod(applicationClass, AutomationResolver.resolve("ApplicationLoader", "onCreate", AutomationResolver.ResolverType.Method), new XC_MethodHook(51) {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Application app = (Application) param.thisObject;
                if (app == null)
                    throw new AssertionError("app == null");
                StartupRoutine.execPreStartupInit(app);
            }
        });
        initialized = true;
    }
}