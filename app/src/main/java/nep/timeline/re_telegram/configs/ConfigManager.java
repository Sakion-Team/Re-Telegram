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
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        jsonModule.add("NoSponsoredMessages", new JsonPrimitive(Configs.isNoSponsoredMessages()));
        jsonModule.add("ProhibitChannelSwitching", new JsonPrimitive(Configs.isProhibitChannelSwitching()));
        jsonModule.add("AllowMoveAllChatFolder", new JsonPrimitive(Configs.isAllowMoveAllChatFolder()));
        jsonModule.add("UseSystemTypeface", new JsonPrimitive(Configs.isUseSystemTypeface()));
        jsonModule.add("HideStories", new JsonPrimitive(Configs.isHideStories()));
        FileUtils.save(cfgPath, Utils.getBuilderGson().toJson(valueJsonObject), false);
    }
}
