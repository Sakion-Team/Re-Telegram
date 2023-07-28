package nep.timeline.re_telegram;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ClientChecker {
    public static boolean check(ClientType client, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return lpparam.packageName.equals(client.getPackageName());
    }

    public static boolean check(ClientType client)
    {
        return check(client, Utils.globalLoadPackageParam);
    }

    public enum ClientType {
        Nekogram("tw.nekomimi.nekogram"),
        Yukigram("me.onlyfire.yukigram.beta"),
        MDgram("org.telegram.mdgram");

        final String packageName;

        ClientType(String packageName)
        {
            this.packageName = packageName;
        }

        public String getPackageName()
        {
            return packageName;
        }
    }
}
