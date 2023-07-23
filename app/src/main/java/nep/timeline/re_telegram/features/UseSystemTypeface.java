package nep.timeline.re_telegram.features;

import android.graphics.Typeface;
import android.os.Build;

import java.util.Hashtable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.FieldUtils;

public class UseSystemTypeface {
    public static void init(XC_LoadPackage.LoadPackageParam lpparam)
    {
        Class<?> androidUtilities = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.AndroidUtilities"), lpparam.classLoader);
        XposedHelpers.findAndHookMethod(androidUtilities, AutomationResolver.resolve("AndroidUtilities", "getTypeface", AutomationResolver.ResolverType.Method), String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Hashtable<String, Typeface> typefaceCache = (Hashtable<String, Typeface>) FieldUtils.getFieldFromMultiName(androidUtilities, AutomationResolver.resolve("AndroidUtilities", "typefaceCache", AutomationResolver.ResolverType.Field), Hashtable.class).get(param.thisObject);
                synchronized (typefaceCache) {
                    String assetPath = (String) param.args[0];
                    if (Configs.isUseSystemTypeface() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (assetPath.contains("medium") && assetPath.contains("italic")) {
                            param.setResult(Typeface.create("sans-serif-medium", Typeface.ITALIC));
                            return;
                        }
                        if (assetPath.contains("medium")) {
                            param.setResult(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                            return;
                        }
                        if (assetPath.contains("italic")) {
                            param.setResult(Typeface.create((Typeface) null, Typeface.ITALIC));
                            return;
                        }
                        if (assetPath.contains("mono")) {
                            param.setResult(Typeface.MONOSPACE);
                            return;
                        }
                        if (assetPath.contains("mw_bold")) {
                            param.setResult(Typeface.create("serif", Typeface.BOLD));
                            return;
                        }
                        //param.setResult(Typeface.create((Typeface) null, Typeface.NORMAL));
                        //return;
                    }
                }
            }
        });
    }
}
