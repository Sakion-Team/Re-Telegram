package nep.timeline.re_telegram.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import nep.timeline.re_telegram.Utils;

public class FieldUtils {
    public static Field getFieldFromMultiName(Class<?> clazz, String fieldName, Object Type)
    {
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields())
            if (declaredField.getName().equals(fieldName))
            {
                if (!declaredField.isAccessible())
                    declaredField.setAccessible(true);

                fields.add(declaredField);
            }

        if (!fields.isEmpty()) {
            Field target = null;
            for (Field field : fields) {
                if (field.getType().equals(Type))
                {
                    target = field;
                }
            }
            if (target != null)
                return target;
            else
                Utils.log("Not found " + fieldName + " field in " + clazz.getName() + ", " + Utils.issue);
        }
        else
            Utils.log("Not found " + fieldName + " field in " + clazz.getName() + ", " + Utils.issue);

        return null;
    }

    public static Field getFieldOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field;
        }
        catch (NoSuchFieldException e)
        {
            Utils.log(e);
            e.printStackTrace();
            return null;
        }
    }

    public static double getFieldDoubleOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getDouble(clazz);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Double.MIN_VALUE;
        }
    }

    public static double getFieldDoubleOfClass(Class<?> clazz, Object instance, String fieldName) {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getDouble(instance);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Double.MIN_VALUE;
        }
    }

    public static float getFieldFloatOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getFloat(clazz);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Float.MIN_VALUE;
        }
    }

    public static float getFieldFloatOfClass(Object instance, Class<?> clazz, String fieldName) {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getFloat(instance);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Float.MIN_VALUE;
        }
    }

    public static int getFieldIntOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getInt(clazz);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public static int getFieldIntOfClass(Object instance, Class<?> clazz, String fieldName) {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getInt(instance);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public static long getFieldLongOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getLong(clazz);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Long.MIN_VALUE;
        }
    }

    public static long getFieldLongOfClass(Object instance, Class<?> clazz, String fieldName) {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.getLong(instance);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return Long.MIN_VALUE;
        }
    }

    public static Object getFieldClassOfClass(Object clazz, String fieldName) {
        try
        {
            Field field = clazz.getClass().getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.get(clazz);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return null;
        }
    }

    public static Object getFieldClassOfClass(Object instance, Class<?> clazz, String fieldName) {
        try
        {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAccessible())
                field.setAccessible(true);

            return field.get(instance);
        }
        catch (Exception e)
        {
            Utils.log(e);
            e.printStackTrace();
            return null;
        }
    }

    public static void setFinalField(Field field, Object instance, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        Field modifiersField = Field.class.getDeclaredField("modifiers");

        if (!modifiersField.isAccessible())
            modifiersField.setAccessible(true);

        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        if (!field.isAccessible())
            field.setAccessible(true);

        field.set(instance, newValue);
    }

    public static void setField(Field field, Object instance, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        if (!field.isAccessible())
            field.setAccessible(true);

        field.set(instance, newValue);
    }
}
