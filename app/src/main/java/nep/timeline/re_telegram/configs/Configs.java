package nep.timeline.re_telegram.configs;

public class Configs {
    private static boolean antiAntiForward = true;
    private static boolean antiRecall = true;
    private static String antiRecallText = "";
    private static boolean antiRecallTextColorful = true;
    private static int antiRecallTextRed = 255;
    private static int antiRecallTextGreen = 0;
    private static int antiRecallTextBlue = 0;
    private static boolean noSponsoredMessages = true;
    private static boolean prohibitChannelSwitching = true;
    private static boolean allowMoveAllChatFolder = true;
    private static boolean useSystemTypeface = true;
    private static boolean hideStories = false;
    private static boolean unlockedNoPremiumAccountsLimit = true;

    public static boolean isAntiAntiForward() {
        return antiAntiForward;
    }

    public static boolean isAntiRecall() {
        return antiRecall;
    }

    public static String getAntiRecallText() {
        return antiRecallText;
    }

    public static boolean isAntiRecallTextColorful() {
        return antiRecallTextColorful;
    }

    public static int getAntiRecallTextRed() {
        return Math.max(Math.min(255, antiRecallTextRed), 0);
    }

    public static int getAntiRecallTextGreen() {
        return Math.max(Math.min(255, antiRecallTextGreen), 0);
    }

    public static int getAntiRecallTextBlue() {
        return Math.max(Math.min(255, antiRecallTextBlue), 0);
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

    public static boolean isHideStories() {
        return hideStories;
    }

    public static boolean isUnlockedNoPremiumAccountsLimit() {
        return unlockedNoPremiumAccountsLimit;
    }

    public static void setAntiAntiForward(boolean value) {
        antiAntiForward = value;
        ConfigManager.save();
    }

    public static void setAntiRecallText(String value) {
        antiRecallText = value;
        ConfigManager.save();
    }

    public static void setAntiRecallTextColorful(boolean value) {
        antiRecallTextColorful = value;
    }

    public static void setAntiRecallTextRed(int red) {
        antiRecallTextRed = red;
    }

    public static void setAntiRecallTextGreen(int green) {
        antiRecallTextGreen = green;
    }

    public static void setAntiRecallTextBlue(int blue) {
        antiRecallTextBlue = blue;
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

    public static void setHideStories(boolean value) {
        hideStories = value;
        ConfigManager.save();
    }

    public static void setUnlockedNoPremiumAccountsLimit(boolean value) {
        unlockedNoPremiumAccountsLimit = value;
        ConfigManager.save();
    }
}
