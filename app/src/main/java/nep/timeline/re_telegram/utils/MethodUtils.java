package nep.timeline.re_telegram.utils;

import java.lang.reflect.Method;

public class MethodUtils {
    public static Object invokeMethodOfClass(Object clazz, String methodName, Object... args) {
        try
        {
            Method method = clazz.getClass().getDeclaredMethod(methodName);

            if (!method.isAccessible())
                method.setAccessible(true);

            return method.invoke(clazz, args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
