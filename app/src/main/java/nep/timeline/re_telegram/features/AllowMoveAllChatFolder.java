package nep.timeline.re_telegram.features;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.HookUtils;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class AllowMoveAllChatFolder {
    private static void loadClassByPath(String root, String path, List<Class<?>> list, ClassLoader load) {
        File f = new File(path);
        if(root == null)
            root = f.getPath();
        if (f.isFile() && f.getName().matches("^.*\\.class$") && f.getPath().contains("org.telegram.ui.DialogsActivity")) {
            try {
                String classPath = f.getPath();
                String className = classPath.substring(root.length() + 1,classPath.length() - 6).replace('/', '.').replace('\\', '.');
                list.add(load.loadClass(className));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            File[] fs = f.listFiles();
            if (fs == null) return;
            for (File file : fs) {
                loadClassByPath(root,file.getPath(), list, load);
            }
        }
    }

    private static List<Class<?>> loadClassByLoader(ClassLoader load) throws IOException {
        Enumeration<URL> urls = load.getResources("");
        List<Class<?>> classes = new ArrayList<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url.getProtocol().equals("file")) {
                loadClassByPath(null, url.getPath(), classes, load);
            }
        }
        return classes;
    }

    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        String onDefaultTabMoved = AutomationResolver.resolve("DialogsActivity", "onDefaultTabMoved", AutomationResolver.ResolverType.Method);
        Class<?> dialogsActivity = null;
        String dialogsActivityName = AutomationResolver.resolve("org.telegram.ui.DialogsActivity");
        if (dialogsActivityName.equals("org.telegram.ui.DialogsActivity")) {
            for (int i = 0; i < 13; ++i)
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

        Class<?> filtersSetupActivity = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.Components.FilterTabsView$TouchHelperCallback"), lpparam.classLoader);
        if (filtersSetupActivity != null)
        {
            String onSelectedChanged = AutomationResolver.resolve("FilterTabsView$TouchHelperCallback", "onSelectedChanged", AutomationResolver.ResolverType.Method);
            HookUtils.findAndHookAllMethod(filtersSetupActivity, onSelectedChanged, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (Configs.isAllowMoveAllChatFolder())
                        param.setResult(null);
                }
            });
        }
        else
        {
            Utils.log("Not found FiltersSetupActivity, " + Utils.issue);
        }
    }
}
