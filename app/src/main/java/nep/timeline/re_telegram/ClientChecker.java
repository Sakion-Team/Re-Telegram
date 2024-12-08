package nep.timeline.re_telegram;

import java.util.Arrays;

public class ClientChecker {
    public static boolean check(ClientType client, String pkgName)
    {
        return Arrays.asList(client.getPackageNames()).contains(pkgName);
    }

    public static boolean check(ClientType client)
    {
        return check(client, Utils.pkgName);
    }

    public enum ClientType {
        Nekogram("tw.nekomimi.nekogram"),
        Cherrygram("uz.unnarsx.cherrygram"),
        Yukigram("me.onlyfire.yukigram.beta"),
        MDgram(new String[]{ "org.telegram.mdgram" });

        final String[] packageNames;

        ClientType(String packageName)
        {
            this.packageNames = new String[]{ packageName };
        }

        ClientType(String[] packageNames)
        {
            this.packageNames = packageNames;
        }

        public String[] getPackageNames()
        {
            return packageNames;
        }
    }
}
