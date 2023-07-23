package nep.timeline.re_telegram.configs;

public class Configs {
    private static boolean antiAntiForward = true;
    private static boolean antiRecall = true;
    private static boolean noSponsoredMessages = true;
    private static boolean prohibitChannelSwitching = true;
    private static boolean allowMoveAllChatFolder = true;
    private static boolean useSystemTypeface = true;

    public static boolean isAntiAntiForward() {
        return antiAntiForward;
    }

    public static boolean isAntiRecall() {
        return antiRecall;
    }

    public static boolean isNoSponsoredMessages() {
        return noSponsoredMessages;
    }

    public static boolean isProhibitChannelSwitching() {
        return prohibitChannelSwitching;
    }

    public static boolean isAllowMoveAllChatFolder() {
        return allowMoveAllChatFolder;
    }

    public static boolean isUseSystemTypeface() {
        return useSystemTypeface;
    }

    public static void setAntiAntiForward(boolean value) {
        antiAntiForward = value;
        ConfigManager.save();
    }

    public static void setAntiRecall(boolean value) {
        antiRecall = value;
        ConfigManager.save();
    }

    public static void setNoSponsoredMessages(boolean value) {
        noSponsoredMessages = value;
        ConfigManager.save();
    }

    public static void setProhibitChannelSwitching(boolean value) {
        prohibitChannelSwitching = value;
        ConfigManager.save();
    }

    public static void setAllowMoveAllChatFolder(boolean value) {
        allowMoveAllChatFolder = value;
        ConfigManager.save();
    }

    public static void setUseSystemTypeface(boolean value) {
        useSystemTypeface = value;
        ConfigManager.save();
    }
}
