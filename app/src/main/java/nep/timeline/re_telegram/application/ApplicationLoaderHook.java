package nep.timeline.re_telegram.application;

import android.app.Application;
import android.os.Build;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class ApplicationLoaderHook {
    private static boolean initialized = false;

    public static void init(ClassLoader loader) {
        // our minSdk is 21 so there is no need to wait for MultiDex to initialize
        if (initialized)
            return;

        Class<?> applicationClass = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.ApplicationLoader"), loader);
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

                new File(app.getFilesDir().getAbsolutePath() + "/DeletedMessages").mkdirs();
                Utils.deletedMessagesSavePath = new File(app.getFilesDir().getAbsolutePath() + "/DeletedMessages/messages.tgar");
                Utils.readDeletedMessages();

                ApplicationInfo.setApplication(app);
            }
        });
        initialized = true;
    }
}