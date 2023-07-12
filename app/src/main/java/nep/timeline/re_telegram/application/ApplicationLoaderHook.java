package nep.timeline.re_telegram.application;

import android.app.Application;

import java.io.File;
import java.io.IOException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.configs.ConfigManager;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class ApplicationLoaderHook {
    private static boolean initialized = false;

    public static void init(ClassLoader loader) {
        // our minSdk is 21 so there is no need to wait for MultiDex to initialize
        if (initialized)
            return;

        Class<?> applicationClass = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.ApplicationLoader"), loader);
        if (applicationClass == null)
            applicationClass = XposedHelpers.findClassIfExists("org.telegram.messenger.ApplicationLoaderImpl", loader);
        if (applicationClass == null)
            applicationClass = XposedHelpers.findClassIfExists("org.thunderdog.challegram.BaseApplication", loader);
        if (applicationClass == null) {
            Utils.log("Not found ApplicationLoader, " + Utils.issue);
            return;
        }
        XposedHelpers.findAndHookMethod(applicationClass, AutomationResolver.resolve("ApplicationLoader", "onCreate", AutomationResolver.ResolverType.Method), new XC_MethodHook(51) {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Application app = (Application) param.thisObject;
                if (app == null)
                {
                    Utils.log("ApplicationLoader is wrong, " + Utils.issue);
                    return;
                }

                new File(app.getFilesDir().getAbsolutePath() + "/ReTelegram").mkdirs();
                Utils.deletedMessagesSavePath = new File(app.getFilesDir().getAbsolutePath() + "/ReTelegram/deletedMessages.list");
                ConfigManager.cfgPath = new File(app.getFilesDir().getAbsolutePath() + "/ReTelegram/configs.cfg");
                try
                {
                    if (!ConfigManager.cfgPath.exists())
                    {
                        ConfigManager.cfgPath.createNewFile();
                        ConfigManager.save();
                    }
                }
                catch (IOException e)
                {
                    Utils.log(e);
                }
                Utils.readDeletedMessages();
                ConfigManager.read();
                ConfigManager.save();

                HostApplicationInfo.setApplication(app);
            }
        });
        initialized = true;
    }
}