package nep.timeline.re_telegram.features;

import android.os.Handler;
import android.os.Looper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import nep.timeline.re_telegram.Utils;

public class SpeedBoostHook {
    private final static long DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2000L;
    private static long speedUpShown = 0;

    public static void init(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod("org.telegram.messenger.FileLoadOperation", classLoader, "updateParams", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    int downloadChunkSizeBig = 1024 * 1024;
                    int maxDownloadRequests = 12;
                    int maxDownloadRequestsBig = 12;

                    var maxCdnParts = (int) (DEFAULT_MAX_FILE_SIZE / downloadChunkSizeBig);
                    XposedHelpers.setIntField(param.thisObject, "downloadChunkSizeBig", downloadChunkSizeBig);
                    XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequests", maxDownloadRequests);
                    XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequestsBig", maxDownloadRequestsBig);
                    XposedHelpers.setObjectField(param.thisObject, "maxCdnParts", maxCdnParts);

                    try {
                        var fileSize = XposedHelpers.getLongField(param.thisObject, "totalBytesCount");
                        if (fileSize > 5 * 1024 * 1024 && System.currentTimeMillis() - speedUpShown > 1000 * 60 * 2) {
                            speedUpShown = System.currentTimeMillis();
                            var title = "Speed Boost Activated";
                            var subtitle = "Extreme Speed Mode Enabled";
                            try {
                                var notificationCenterClass = XposedHelpers.findClass("org.telegram.messenger.NotificationCenter", classLoader);
                                var globalInstance = XposedHelpers.callStaticMethod(notificationCenterClass, "getGlobalInstance");
                                new Handler(Looper.getMainLooper()).post(() -> XposedHelpers.callMethod(
                                        globalInstance,
                                        "postNotificationName",
                                        XposedHelpers.getStaticIntField(notificationCenterClass, "showBulletin"),
                                        new Object[]{4, title, subtitle}));
                            } catch (Throwable t) {
                                XposedBridge.log(t);
                            }
                        }
                    } catch (Throwable t) {
                        XposedBridge.log(t);
                    }
                }
            });
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }
}
