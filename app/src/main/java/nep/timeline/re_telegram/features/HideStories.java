package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class HideStories {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> StoriesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Stories.StoriesController"), lpparam.classLoader);
        if (StoriesController != null)
            HookUtils.findAndHookMethod(StoriesController, AutomationResolver.resolve("hasStories"), new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam)
                {
                    return !Configs.isHideStories();
                }
            });
    }
}
