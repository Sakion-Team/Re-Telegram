package nep.timeline.re_telegram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.application.ApplicationLoaderHook;
import nep.timeline.re_telegram.features.AllowMoveAllChatFolder;
import nep.timeline.re_telegram.features.AntiAntiForward;
import nep.timeline.re_telegram.features.AntiRecallWithDatabase;
import nep.timeline.re_telegram.features.HideStories;
import nep.timeline.re_telegram.features.NEWAntiRecall;
import nep.timeline.re_telegram.features.NoSponsoredMessages;
import nep.timeline.re_telegram.features.ProhibitChannelSwitching;
import nep.timeline.re_telegram.features.UseSystemTypeface;

public class HookInit implements IXposedHookLoadPackage {
    private static final List<String> hookPackages = Arrays.asList("org.telegram.messenger", "org.telegram.messenger.web", "org.telegram.messenger.beta", "org.telegram.plus", "org.telegram.mdgram",
            "tw.nekomimi.nekogram",
            "com.cool2645.nekolite",
            "com.exteragram.messenger",
            "org.forkgram.messenger",
            "org.forkclient.messenger",
            "org.forkclient.messenger.beta",
            "uz.unnarsx.cherrygram",
            "me.onlyfire.yukigram.beta",
            "com.iMe.android.web",
            "com.radolyn.ayugram",
            "it.octogram.android",
            "xyz.nextalone.nnngram",
            "it.belloworld.mercurygram");
    private static final List<String> notNeedHideStories = Arrays.asList("tw.nekomimi.nekogram", "com.exteragram.messenger", "uz.unnarsx.cherrygram");
    private static final List<String> hookPackagesCustomization = Arrays.asList("xyz.nextalone.nagram",
            "nekox.messenger", "com.xtaolabs.pagergram");
    public static final boolean DEBUG_MODE = false;

    public final List<String> getHookPackages()
    {
        List<String> hookPackagesLocal = new ArrayList<>(hookPackages);
        hookPackagesLocal.addAll(hookPackagesCustomization);
        return hookPackagesLocal;
    }

    private boolean onlyNeedAR(String pkgName)
    {
        return hookPackagesCustomization.contains(pkgName);
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (getHookPackages().contains(lpparam.packageName))
        {
            if (DEBUG_MODE)
                Utils.log("Trying to hook app: " + lpparam.packageName);

            Utils.pkgName = lpparam.packageName;
            ClassLoader classLoader = lpparam.classLoader;

            ApplicationLoaderHook.init(classLoader);

            if (ClientChecker.check(ClientChecker.ClientType.Yukigram)) {
                AntiRecallWithDatabase.initUI(classLoader);

                AntiRecallWithDatabase.initProcessing(classLoader);

                AntiRecallWithDatabase.init(classLoader);
            } else {
                NEWAntiRecall.initUI(classLoader);

                NEWAntiRecall.initProcessing(classLoader);

                NEWAntiRecall.init(classLoader);
            }

            // FakePremium.init(lpparam);

            AllowMoveAllChatFolder.init(classLoader);

            if (!onlyNeedAR(lpparam.packageName))
            {
                if (!ClientChecker.check(ClientChecker.ClientType.MDgram))
                    UseSystemTypeface.init(classLoader);

                if (!lpparam.packageName.equals("xyz.nextalone.nnngram")) {
                    if (!ClientChecker.check(ClientChecker.ClientType.Nekogram))
                        ProhibitChannelSwitching.init(classLoader);

                    if (!notNeedHideStories.contains(lpparam.packageName))
                        HideStories.init(classLoader);

                    NoSponsoredMessages.init(classLoader);
                }

                AntiAntiForward.init(classLoader);
            }
        }
    }
}
