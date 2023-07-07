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
                            path.addRoundRect(0, 0, width, height, width / 2, width / 2, Path.Direction.CW);
                            Paint paint = new Paint();
                            paint.setAntiAlias(true);
                            paint.setColor(Color.TRANSPARENT);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                            canvas.drawPath(path, paint);
                            return PixelFormat.TRANSLUCENT;
                        }));
                        Class<?> y90 = XposedHelpers.findClassIfExists("Y90", Utils.globalLoadPackageParam.classLoader);
                        Bitmap bitmap2 = (Bitmap) MethodUtils.invokeMethodOfClass(y90, "H", bitmap);
                        Class<?> iconCompatClass = XposedHelpers.findClassIfExists(AutomationResolver.resolve("androidx.core.graphics.drawable.IconCompat"), Utils.globalLoadPackageParam.classLoader);
                        Object result = MethodUtils.invokeMethodOfClass(null, iconCompatClass, AutomationResolver.resolve("IconCompat", "b", AutomationResolver.ResolverType.Method), bitmap2);

                        for (Field declaredField : param.args[1].getClass().getDeclaredFields())
                            if (declaredField.getName().equals("b") && declaredField.getType().equals(Object.class))
                                declaredField.set(param.args[1], result);
                    } catch (Throwable t) {
                        Utils.log(t);
                    }
                }

                return null;
            }
        });
    }
}
