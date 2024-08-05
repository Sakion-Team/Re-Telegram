package nep.timeline.re_telegram.virtuals;

import android.text.TextPaint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class Theme {
    public static TextPaint getTextPaint(ClassLoader classLoader)
    {
        Class<?> theme = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.ui.ActionBar.Theme"), classLoader);
        if (!ClientChecker.check(ClientChecker.ClientType.Nekogram) && !ClientChecker.check(ClientChecker.ClientType.Yukigram))
            return (TextPaint) XposedHelpers.getStaticObjectField(theme, AutomationResolver.resolve("Theme", "chat_timePaint", AutomationResolver.ResolverType.Field));
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
                else {
                    for (Field field : fields) {
                        if (field.getType().getName().contains("TextPaint"))
                        {
                            textPaintField = field;
                        }
                    }
                    if (textPaintField != null)
                        return (TextPaint) textPaintField.get(null);
                    else
                        Utils.log("Not found chat_timePaint field in Theme, " + Utils.issue);
                }
            }
            catch (IllegalAccessException e)
            {
                Utils.log(e);
            }
        }
        else
            Utils.log("Not found chat_timePaint field in Theme, " + Utils.issue);

        return null;
    }
}
