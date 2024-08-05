package nep.timeline.re_telegram.base;

import de.robv.android.xposed.XC_MethodHook;
import nep.timeline.re_telegram.Utils;

public abstract class AbstractMethodHook extends XC_MethodHook {
    protected void beforeMethod(MethodHookParam param) throws Throwable {

    }

    protected void afterMethod(MethodHookParam param) throws Throwable {

    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        try {
            beforeMethod(param);
        } catch (Throwable throwable) {
            Utils.log(throwable);
        }
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        try {
            afterMethod(param);
        } catch (Throwable throwable) {
            Utils.log(throwable);
        }
    }
}
