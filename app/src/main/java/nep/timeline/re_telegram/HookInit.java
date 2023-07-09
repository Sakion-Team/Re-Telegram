package nep.timeline.re_telegram;

import android.content.res.XModuleResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.application.ApplicationLoaderHook;
import nep.timeline.re_telegram.features.AntiAntiForward;
import nep.timeline.re_telegram.features.AntiRecall;
import nep.timeline.re_telegram.features.NoSponsoredMessages;
import nep.timeline.re_telegram.features.ProhibitChannelSwitching;
import nep.timeline.re_telegram.obfuscate.AutomationResolver;

public class HookInit implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {
    private static final List<String> hookPackages = Arrays.asList("org.telegram.messenger", "org.telegram.messenger.web", "org.telegram.messenger.beta", "org.telegram.plus", "org.telegram.mdgram",
            "tw.nekomimi.nekogram",
            "com.cool2645.nekolite",
            "com.exteragram.messenger",
            "org.forkclient.messenger",
            "org.forkclient.messenger.beta",
            "uz.unnarsx.cherrygram",
            "me.onlyfire.yukigram.beta");
    private static final List<String> hookPackagesCustomization = Arrays.asList("xyz.nextalone.nagram", "xyz.nextalone.nnngram",
            "nekox.messenger");
    private static String MODULE_PATH = null;
    public static final boolean DEBUG_MODE = true;
    public static final boolean LITE_MODE = false;

    public final List<String> getHookPackages()
    {
        List<String> hookPackagesLocal = new ArrayList<>(hookPackages);
        List<String> hookPackagesCustomizationLocal = new ArrayList<>(hookPackagesCustomization);
        hookPackagesLocal.addAll(hookPackagesCustomizationLocal);
        return hookPackagesLocal;
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        if (!getHookPackages().contains(resparam.packageName))
            return;

        XModuleResources.createInstance(MODULE_PATH, resparam.res);
    }

    private boolean onlyNeedAR(final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return hookPackagesCustomization.contains(lpparam.packageName);
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (getHookPackages().contains(lpparam.packageName)) {
            if (DEBUG_MODE)
                Utils.log("Trying to hook app: " + lpparam.packageName);
            Utils.globalLoadPackageParam = lpparam;
            ApplicationLoaderHook.init(lpparam.classLoader);

            AntiRecall.initUI(lpparam);

            AntiRecall.initNotification(lpparam);

            Class<?> messagesController = XposedHelpers.findClassIfExists(AutomationResolver.resolve("org.telegram.messenger.MessagesController"), lpparam.classLoader);

            if (messagesController != null)
            {
                AntiRecall.init(lpparam, messagesController);

                if (!onlyNeedAR(lpparam))
                {
                    //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P)
                    // if (ClientChecker.isNekogram())
                    // NekogramRoundAvatar.init(); // Bug!

                    ProhibitChannelSwitching.init(lpparam);

                    if (!ClientChecker.isYukigram())
                        NoSponsoredMessages.init(lpparam);

                    AntiAntiForward.init(lpparam, messagesController);
                }
            }
            else
            {
                Utils.log("Not found MessagesController, " + Utils.issue);
            }

            // Fake Premium
            // XposedHelpers.findAndHookMethod("org.telegram.messenger.UserConfig", lpparam.classLoader, "isPremium", XC_MethodReplacement.returnConstant(true));
        }
    }
}
