package nep.timeline.re_telegram.features;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.base.AbstractMethodHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.MethodUtils;

public class AllowMoveAllChatFolder {
    public static void init(ClassLoader classLoader)
    {
        String onDefaultTabMoved = AutomationResolver.resolve("DialogsActivity", "onDefaultTabMoved", AutomationResolver.ResolverType.Method);
        Class<?> dialogsActivity = null;
        String dialogsActivityName = AutomationResolver.resolve("org.telegram.ui.DialogsActivity");
        if (dialogsActivityName.equals("org.telegram.ui.DialogsActivity")) {
            for (int i = 0; i < 51; i++)
            {
                Class<?> dialogsActivity$ = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.DialogsActivity$" + i), classLoader);
                if (dialogsActivity$ != null)
                    for (Method declaredMethod : dialogsActivity$.getDeclaredMethods()) {
                        if (declaredMethod.getName().equals(onDefaultTabMoved))
                        {
                            dialogsActivity = dialogsActivity$;
                            break;
                        }
                    }
            }
        }
        else
            dialogsActivity = XposedHelpers.findClassIfExists(dialogsActivityName, classLoader);

        if (dialogsActivity != null)
        {
            //HookUtils.findAndHookAllMethod(
            XposedHelpers.findAndHookMethod(dialogsActivity, onDefaultTabMoved, new AbstractMethodHook() {
                @Override
                protected void beforeMethod(MethodHookParam param) {
                    if (Configs.isAllowMoveAllChatFolder())
                        param.setResult(null);
                }
            });
        }
        else
        {
            Utils.log("Not found DialogsActivity, " + Utils.issue);
        }

        if (!ClientChecker.check(ClientChecker.ClientType.Nekogram)) {
            Class<?> filtersSetupActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Components.FilterTabsView$TouchHelperCallback"), classLoader);
            if (filtersSetupActivity != null) {
                String run = AutomationResolver.resolve("FilterTabsView$TouchHelperCallback", "onSelectedChanged", AutomationResolver.ResolverType.Method);

                HookUtils.findAndHookMethod(filtersSetupActivity, run, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isAllowMoveAllChatFolder())
                            param.setResult(null);
                    }
                });
            } else {
                Utils.log("Not found FiltersSetupActivity, " + Utils.issue);
            }
        }
        else
        {
            Class<?> filtersSetupActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback"), classLoader);
            if (filtersSetupActivity != null) {
                String onSelectedChanged = AutomationResolver.resolve("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", AutomationResolver.ResolverType.Method);
                XposedHelpers.findAndHookMethod(filtersSetupActivity, onSelectedChanged, new AbstractMethodHook() {
                    @Override
                    protected void beforeMethod(MethodHookParam param) {
                        if (Configs.isAllowMoveAllChatFolder())
                            param.setResult(null);
                    }
                });
            } else {
                Utils.log("Not found FiltersSetupActivity$TouchHelperCallback, you maybe using an unsupported Nekogram version.");
            }
        }
    }
}
