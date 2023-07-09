package nep.timeline.re_telegram;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ClientChecker {
    public static boolean isNekogram(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return lpparam.packageName.equals("tw.nekomimi.nekogram");
    }

    public static boolean isCherrygram(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return lpparam.packageName.equals("uz.unnarsx.cherrygram");
    }

    public static boolean isYukigram(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return lpparam.packageName.equals("me.onlyfire.yukigram.beta");
    }

    public static boolean isNekogram()
    {
        return isNekogram(Utils.globalLoadPackageParam);
    }

    public static boolean isCherrygram()
    {
        return isCherrygram(Utils.globalLoadPackageParam);
    }

    public static boolean isYukigram()
    {
        return isYukigram(Utils.globalLoadPackageParam);
    }
}
