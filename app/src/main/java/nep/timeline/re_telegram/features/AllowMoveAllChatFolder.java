package nep.timeline.re_telegram.features;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class AllowMoveAllChatFolder {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        String onDefaultTabMoved = AutomationResolver.resolve("DialogsActivity", "onDefaultTabMoved", AutomationResolver.ResolverType.Method);
        Class<?> dialogsActivity = null;
        String dialogsActivityName = AutomationResolver.resolve("org.telegram.ui.DialogsActivity");
        if (dialogsActivityName.equals("org.telegram.ui.DialogsActivity")) {
            for (int i = 0; i < 51; i++)
            {
                Class<?> dialogsActivity$ = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.DialogsActivity$" + i), lpparam.classLoader);
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
            dialogsActivity = XposedHelpers.findClassIfExists(dialogsActivityName, lpparam.classLoader);

        if (dialogsActivity != null)
        {
            HookUtils.findAndHookAllMethod(dialogsActivity, onDefaultTabMoved, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
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
            Class<?> filtersSetupActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Components.FilterTabsView$TouchHelperCallback"), lpparam.classLoader);
            if (filtersSetupActivity != null) {
                String run = AutomationResolver.resolve("FilterTabsView$TouchHelperCallback", "onSelectedChanged", AutomationResolver.ResolverType.Method);
                HookUtils.findAndHookAllMethod(filtersSetupActivity, run, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
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
            Class<?> filtersSetupActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.FiltersSetupActivity$TouchHelperCallback"), lpparam.classLoader);
            if (filtersSetupActivity != null) {
                String onSelectedChanged = AutomationResolver.resolve("FiltersSetupActivity$TouchHelperCallback", "resetDefaultPosition", AutomationResolver.ResolverType.Method);
                HookUtils.findAndHookAllMethod(filtersSetupActivity, onSelectedChanged, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
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
