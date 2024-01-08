package nep.timeline.re_telegram;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

public class HookUtils {
    public static XC_MethodHook.Unhook findAndHookMethod(Class<?> clazz, String methodName, XC_MethodHook callback)
    {
        Method resultMethod = null;

        for (Method method : clazz.getDeclaredMethods())
            if (method.getName().equals(methodName))
                resultMethod = method;

        if (resultMethod == null)
            throw new NullPointerException("Not found method " + methodName + " from " + clazz.getName());

        return XposedBridge.hookMethod(resultMethod, callback);
    }

    public static List<XC_MethodHook.Unhook> findAndHookAllMethod(Class<?> clazz, String methodName, XC_MethodHook callback)
    {
        List<XC_MethodHook.Unhook> unhooks = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods())
            if (method.getName().equals(methodName))
                unhooks.add(XposedBridge.hookMethod(method, callback));

        if (unhooks.isEmpty())
            throw new NullPointerException("Not found method " + methodName + " from " + clazz.getName());

        return unhooks;
    }
}
