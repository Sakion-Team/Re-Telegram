package nep.timeline.re_telegram.features;

import android.graphics.Typeface;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.base.AbstractReplaceHook;
import nep.timeline.re_telegram.configs.Configs;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.FieldUtils;

public class UseSystemTypeface {
    public static void init(ClassLoader classLoader)
    {
        Class<?> androidUtilities = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.AndroidUtilities"), classLoader);
        XposedHelpers.findAndHookMethod(androidUtilities, AutomationResolver.resolve("AndroidUtilities", "getTypeface", AutomationResolver.ResolverType.Method), String.class, new AbstractReplaceHook() {
            @Override
            protected Object replaceMethod(MethodHookParam param) throws Throwable {
                Object typefaceCache = FieldUtils.getFieldFromMultiName(androidUtilities, AutomationResolver.resolve("AndroidUtilities", "typefaceCache", AutomationResolver.ResolverType.Field), Hashtable.class).get(param.thisObject);
                synchronized (typefaceCache) {
                    String assetPath = (String) param.args[0];
                    if (Configs.isUseSystemTypeface() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (assetPath.contains("medium") && assetPath.contains("italic")) {
                            return Typeface.create("sans-serif-medium", Typeface.ITALIC);
                        }
                        if (assetPath.contains("medium")) {
                            return Typeface.create("sans-serif-medium", Typeface.NORMAL);
                        }
                        if (assetPath.contains("italic")) {
                            return Typeface.create((Typeface) null, Typeface.ITALIC);
                        }
                        if (assetPath.contains("mono")) {
                            return Typeface.MONOSPACE;
                        }
                        if (assetPath.contains("mw_bold")) {
                            return Typeface.create("serif", Typeface.BOLD);
                        }
                        // return Typeface.create((Typeface) null, Typeface.NORMAL);
                    }

                    try {
                        return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                    } catch (InvocationTargetException ex) {
                        throw ex.getTargetException();
                    }
                }
            }
        });
    }
}
