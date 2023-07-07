package nep.timeline.re_telegram.obfuscate;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.ClientChecker;
import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.obfuscate.resolves.Nekogram;

public class AutomationResolver {
    public static String resolve(String className, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        if (ClientChecker.isNekogram(lpparam))
        {
            if (Nekogram.ClassResolver.has(className))
                return Nekogram.ClassResolver.resolve(className);
        }

        return className;
    }

    public static String resolve(String className, String name, ResolverType type, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        if (ClientChecker.isNekogram(lpparam))
        {
            if (type == ResolverType.Field)
            {
                if (Nekogram.FieldResolver.has(className, name))
                    return Nekogram.FieldResolver.resolve(className, name);
            }
            else if (type == ResolverType.Method)
            {
                if (Nekogram.MethodResolver.has(className, name))
                    return Nekogram.MethodResolver.resolve(className, name);
            }
        }

        return name;
    }

    public static String resolve(String className)
    {
        return resolve(className, Utils.globalLoadPackageParam);
    }

    public static String resolve(String className, String name, ResolverType type)
    {
        return resolve(className, name, type, Utils.globalLoadPackageParam);
    }

    public enum ResolverType
    {
        Field,
        Method
    }
}
