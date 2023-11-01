package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class HideStories {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> StoriesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Stories.StoriesController"), lpparam.classLoader);
        if (StoriesController != null)
            XposedHelpers.findAndHookMethod(StoriesController, AutomationResolver.resolve("StoriesController", "hasStories", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam)
                {
                    if (Configs.isHideStories())
                        methodHookParam.setResult(false);
                }
            });

        Class<?> MessagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), lpparam.classLoader);
        if (MessagesController != null)
            XposedHelpers.findAndHookMethod(MessagesController, AutomationResolver.resolve("MessagesController", "storiesEnabled", AutomationResolver.ResolverType.Method), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam methodHookParam)
                {
                    if (Configs.isHideStories())
                        methodHookParam.setResult(false);
                }
            });
    }
}
