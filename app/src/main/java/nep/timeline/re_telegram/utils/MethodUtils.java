package nep.timeline.re_telegram.utils;

import java.lang.reflect.Method;
import java.util.Objects;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;

public class MethodUtils {
    public static Object[] findParameterTypesFromClass(ClassLoader classLoader, String clazzName, String methodName) {
        for (Method method : XposedHelpers.findClassIfExists(clazzName, classLoader).getDeclaredMethods())
            if (method.getName().equals(methodName))
                return method.getParameterTypes();
        return null;
    }

    public static Object[] findParameterTypesOrDefault(Class<?> clazz, String methodName, Object... parameter) {
        try {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameter.length <= parameterTypes.length) {
                        boolean isDone = true;
                        for (int i = 0; i < parameter.length; i++) {
                            Object obj = parameter[i];
                            if (!Objects.equals(obj, obj instanceof String ? parameterTypes[i].getName() : parameterTypes[i])) {
                                isDone = false;
                            }
                        }
                        if (isDone)
                            return method.getParameterTypes();
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return parameter;
    }

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
