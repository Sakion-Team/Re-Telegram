package nep.timeline.re_telegram.base;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import nep.timeline.re_telegram.Utils;

public class AbstractReplaceHook extends XC_MethodReplacement {
    protected Object replaceMethod(MethodHookParam param) throws Throwable {
        return null;
    }

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        try {
            return replaceMethod(param);
        } catch (Throwable throwable) {
            Utils.log(throwable);
        }
        try {
            return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
}
