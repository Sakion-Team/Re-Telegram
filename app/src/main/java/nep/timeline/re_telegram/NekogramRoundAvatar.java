package nep.timeline.re_telegram;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;

import java.io.File;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

import nep.timeline.re_telegram.obfuscate.AutomationResolver;
import nep.timeline.re_telegram.utils.MethodUtils;

public class NekogramRoundAvatar {
    @TargetApi(Build.VERSION_CODES.P)
    public static void init()
    {
        Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.NotificationsController"), Utils.globalLoadPackageParam.classLoader);
        HookUtils.findAndHookMethod(messagesController, AutomationResolver.resolve("NotificationsController", "loadRoundAvatar", AutomationResolver.ResolverType.Method), new XC_MethodReplacement()
        {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                File avatar = (File) param.args[0];
                if (avatar != null) {
                    try {
                        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(avatar), (decoder, info, src) -> decoder.setPostProcessor((canvas) -> {
                            Path path = new Path();
                            path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
                            int width = canvas.getWidth();
                            int height = canvas.getHeight();
                            path.addRoundRect(0, 0, width, height, width / 2f, width / 2f, Path.Direction.CW);
                            Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            paint.setColor(Color.TRANSPARENT);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                            canvas.drawPath(path, paint);
                            return PixelFormat.TRANSLUCENT;
                        }));

                        Class<?> iconCompatClass = XposedHelpers.findClassIfExists(AutomationResolver.resolve("androidx.core.graphics.drawable.IconCompat"), Utils.globalLoadPackageParam.classLoader);
                        Object result = MethodUtils.invokeMethodOfClass(null, iconCompatClass, AutomationResolver.resolve("IconCompat", "b", AutomationResolver.ResolverType.Method), bitmap);

                        //Person.Builder builder = (Person.Builder) param.args[1];

                        //builder.setIcon((Icon) result);

                        Object builder = param.args[1];

                        for (Field declaredField : builder.getClass().getDeclaredFields())
                            if (declaredField.getName().equals("b") && declaredField.getType().equals(Class.class))
                            {
                                declaredField.set(builder, result);
                            }
                            else
                                Utils.log("Not found mIcon from Person.Builder, " + Utils.issue);
                    } catch (Throwable t) {
                        Utils.log(t);
                    }
                }

                return null;
            }
        });
    }
}
