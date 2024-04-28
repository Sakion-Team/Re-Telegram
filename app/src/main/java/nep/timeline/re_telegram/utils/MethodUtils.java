package nep.timeline.re_telegram.utils;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;

public class MethodUtils {
    public static Object invokeMethodOfClass(Object clazz, String methodName, Object... args) {
        try
        {
            return XposedHelpers.callMethod(clazz, methodName, args);
            /*Method method = clazz.getClass().getDeclaredMethod(methodName);

            if (!method.isAccessible())
                method.setAccessible(true);

            return method.invoke(clazz, args);*/
        }
        catch (Exception e)
        {
            Utils.log(e);
            return null;
        }
    }

    /*public static Object invokeMethodOfClass(Object instance, Class<?> clazz, String methodName, Object... args) {
        try
        {
            Method method = clazz.getDeclaredMethod(methodName);

            if (!method.isAccessible())
                method.setAccessible(true);

            return method.invoke(instance, args);
        }
        catch (Exception e)
        {
            Utils.log(e);
            return null;
        }
    }

    public static Object invokeMethodOfClass(Object clazz, Method method, Object... args) {
        try
        {
            if (!method.isAccessible())
                method.setAccessible(true);

            return method.invoke(clazz, args);
        }
        catch (Exception e)
        {
            Utils.log(e);
            return null;
        }
    }*/
}
