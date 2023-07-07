package nep.timeline.re_telegram.TMoe;

import android.app.Application;
import android.os.Build;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.io.File;

import nep.timeline.re_telegram.Utils;

public class StartupRoutine {
    public static void execPreStartupInit(Application application) {
        // native library was already loaded before this method is called
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            HiddenApiBypass.setHiddenApiExemptions("L");

        new File(application.getFilesDir().getAbsolutePath() + "/DeletedMessages").mkdirs();
        Utils.deletedMessagesSavePath = new File(application.getFilesDir().getAbsolutePath() + "/DeletedMessages/messages.tgar");
        Utils.readDeletedMessages();

        HostInfo.setHostApplication(application);
    }
}