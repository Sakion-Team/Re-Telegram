package nep.timeline.re_telegram.viruals;

import android.text.TextPaint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class Theme {
    private final Object instance;

    public Theme(Object instance)
    {
        this.instance = instance;
    }

    public static TextPaint getTextPaint()
    {
        Class<?> theme = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ActionBar.Theme"), Utils.globalLoadPackageParam.classLoader);
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : theme.getDeclaredFields())
            if (declaredField.getName().equals(AutomationResolver.resolve("Theme", "chat_timePaint", AutomationResolver.ResolverType.Field)))
                fields.add(declaredField);

        if (!fields.isEmpty()) {
            try
            {
                Field textPaintField = null;
                for (Field field : fields) {
                    if (field.getType().equals(TextPaint.class))
                    {
                        textPaintField = field;
                    }
                }
                if (textPaintField != null)
                    return (TextPaint) textPaintField.get(null);
                else
                    XposedBridge.log("[TGAR Error] Not found chat_timePaint field in Theme, " + Utils.issue);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        else
            XposedBridge.log("[TGAR Error] Not found chat_timePaint field in Theme, " + Utils.issue);

        return null;
    }
}
