package nep.timeline.re_telegram.features;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class HideStories {
    public static void init(ClassLoader classLoader)
    {
        Class<?> StoriesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Stories.StoriesController"), classLoader);
        if (StoriesController != null)
            XposedHelpers.findAndHookMethod(StoriesController, AutomationResolver.resolve("StoriesController", "hasStories", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam methodHookParam)
                {
                    if (Configs.isHideStories())
                        methodHookParam.setResult(false);
                }
            });

        Class<?> MessagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), classLoader);
        if (MessagesController != null)
            XposedHelpers.findAndHookMethod(MessagesController, AutomationResolver.resolve("MessagesController", "storiesEnabled", AutomationResolver.ResolverType.Method), new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam methodHookParam)
                {
                    if (Configs.isHideStories())
                        methodHookParam.setResult(false);
                }
            });
    }
}
