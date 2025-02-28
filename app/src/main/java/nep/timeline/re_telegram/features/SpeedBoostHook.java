package nep.timeline.re_telegram.features;
import android.os.Handler;
import android.os.Looper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class SpeedBoostHook {
    private final static long DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2000L;
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
                }
            });
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }
}
