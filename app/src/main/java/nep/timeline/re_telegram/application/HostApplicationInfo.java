package nep.timeline.re_telegram.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Objects;

public class HostApplicationInfo {

    private static Application sHostApplication = null;
    private static PackageInfo sHostPackageInfo = null;
    private static String sHostAppPackageName = null;
    private static int sHostVersionCode = 0;
    private static long sHostLongVersionCode = 0;
    private static String sHostAppVersionName = null;
    private static String sHostAppName = null;

    public static void setApplication(Application app) {
        Objects.requireNonNull(app, "app");
        sHostApplication = app;
        try {
            sHostPackageInfo = app.getPackageManager().getPackageInfo(app.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // should not happen, we must be installed
            throw new IllegalStateException(e);
        }
        sHostAppPackageName = app.getPackageName();
        sHostVersionCode = sHostPackageInfo.versionCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            sHostLongVersionCode = sHostPackageInfo.getLongVersionCode();
        } else {
            sHostLongVersionCode = sHostVersionCode;
        }
        sHostAppVersionName = sHostPackageInfo.versionName;
        sHostAppName = sHostPackageInfo.applicationInfo.loadLabel(app.getPackageManager()).toString();
    }

    public static Application getApplication() {
        return sHostApplication;
    }

    public static String getPackageName() {
        return sHostAppPackageName;
    }

    public static String getAppName() {
        return sHostAppName;
    }

    public static String getVersionName() {
        return sHostAppVersionName;
    }

    public static PackageInfo getPackageInfo() {
        return sHostPackageInfo;
    }

    public static int getVersionCode() {
        return sHostVersionCode;
    }

    public static long getLongVersionCode() {
        return sHostLongVersionCode;
    }
}