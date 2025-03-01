package nep.timeline.re_telegram.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import nep.timeline.re_telegram.Utils;
import nep.timeline.re_telegram.utils.FileUtils;

public class ConfigManager {
    public static File cfgPath = null;

    public static void read()
    {
        if (cfgPath == null)
            return;

        try
        {
            JsonElement valueJsonElement = JsonParser.parseReader(new BufferedReader(new FileReader(cfgPath)));
            if (!valueJsonElement.isJsonNull() && valueJsonElement instanceof JsonObject)
            {
                JsonObject jsonObject = (JsonObject) valueJsonElement;
                jsonObject.entrySet().forEach(entry -> {
                    JsonObject jsonModule = entry.getValue().getAsJsonObject();
                    if (jsonModule.get("AntiAntiForward") != null)
                        Configs.setAntiAntiForward(jsonModule.get("AntiAntiForward").getAsBoolean());
                    if (jsonModule.get("AntiRecall") != null)
                        Configs.setAntiRecall(jsonModule.get("AntiRecall").getAsBoolean());
                    if (jsonModule.get("AntiRecallText") != null)
                        Configs.setAntiRecallText(jsonModule.get("AntiRecallText").getAsString());
                    if (jsonModule.get("AntiRecallTextColorful") != null)
                        Configs.setAntiRecallTextColorful(jsonModule.get("AntiRecallTextColorful").getAsBoolean());
                    if (jsonModule.get("AntiRecallTextRed") != null)
                        Configs.setAntiRecallTextRed(jsonModule.get("AntiRecallTextRed").getAsInt());
                    if (jsonModule.get("AntiRecallTextGreen") != null)
                        Configs.setAntiRecallTextGreen(jsonModule.get("AntiRecallTextGreen").getAsInt());
                    if (jsonModule.get("AntiRecallTextBlue") != null)
                        Configs.setAntiRecallTextBlue(jsonModule.get("AntiRecallTextBlue").getAsInt());
                    if (jsonModule.get("NoSponsoredMessages") != null)
                        Configs.setNoSponsoredMessages(jsonModule.get("NoSponsoredMessages").getAsBoolean());
                    if (jsonModule.get("ProhibitChannelSwitching") != null)
                        Configs.setProhibitChannelSwitching(jsonModule.get("ProhibitChannelSwitching").getAsBoolean());
                    if (jsonModule.get("AllowMoveAllChatFolder") != null)
                        Configs.setAllowMoveAllChatFolder(jsonModule.get("AllowMoveAllChatFolder").getAsBoolean());
                    if (jsonModule.get("UseSystemTypeface") != null)
                        Configs.setUseSystemTypeface(jsonModule.get("UseSystemTypeface").getAsBoolean());
                    if (jsonModule.get("HideStories") != null)
                        Configs.setHideStories(jsonModule.get("HideStories").getAsBoolean());
                    if (jsonModule.get("SpeedBoost") != null)
                        Configs.setSpeedBoost(jsonModule.get("SpeedBoost").getAsBoolean());
                    //if (jsonModule.get("UnlockedNoPremiumAccountsLimit") != null)
                    //    Configs.setUnlockedNoPremiumAccountsLimit(jsonModule.get("UnlockedNoPremiumAccountsLimit").getAsBoolean());
                });
            }
        }
        catch (IOException e)
        {
            Utils.log(e);
        }
    }

    public static void save()
    {
        if (cfgPath == null)
            return;

        JsonObject valueJsonObject = new JsonObject();
        JsonObject jsonModule = new JsonObject();
        valueJsonObject.add("Re-Telegram", jsonModule);
        jsonModule.add("AntiAntiForward", new JsonPrimitive(Configs.isAntiAntiForward()));
        jsonModule.add("AntiRecall", new JsonPrimitive(Configs.isAntiRecall()));
        jsonModule.add("AntiRecallText", new JsonPrimitive(Configs.getAntiRecallText()));
        jsonModule.add("AntiRecallTextColorful", new JsonPrimitive(Configs.isAntiRecallTextColorful()));
        jsonModule.add("AntiRecallTextRed", new JsonPrimitive(Configs.getAntiRecallTextRed()));
        jsonModule.add("AntiRecallTextGreen", new JsonPrimitive(Configs.getAntiRecallTextGreen()));
        jsonModule.add("AntiRecallTextBlue", new JsonPrimitive(Configs.getAntiRecallTextBlue()));
        jsonModule.add("NoSponsoredMessages", new JsonPrimitive(Configs.isNoSponsoredMessages()));
        jsonModule.add("ProhibitChannelSwitching", new JsonPrimitive(Configs.isProhibitChannelSwitching()));
        jsonModule.add("AllowMoveAllChatFolder", new JsonPrimitive(Configs.isAllowMoveAllChatFolder()));
        jsonModule.add("UseSystemTypeface", new JsonPrimitive(Configs.isUseSystemTypeface()));
        jsonModule.add("HideStories", new JsonPrimitive(Configs.isHideStories()));
        jsonModule.add("SpeedBoost", new JsonPrimitive(Configs.isSpeedBoost()));
        //jsonModule.add("UnlockedNoPremiumAccountsLimit", new JsonPrimitive(Configs.isUnlockedNoPremiumAccountsLimit()));
        FileUtils.save(cfgPath, Utils.getBuilderGson().toJson(valueJsonObject), false);
    }
}
