package nep.timeline.re_telegram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nep.timeline.re_telegram.features.AntiRecall;
import nep.timeline.re_telegram.structs.DeletedMessageInfo;
import nep.timeline.re_telegram.utils.FileUtils;

public class Utils {
    public static XC_LoadPackage.LoadPackageParam globalLoadPackageParam = null;
    public static final String issue = "Your telegram may have been modified! You can submit issue to let developer to try support to the telegram client you are using.";
    private static final Gson BUILDER_GSON = new GsonBuilder().setPrettyPrinting().create();
    public static File deletedMessagesSavePath = null;

    public static void log(String text)
    {
        XposedBridge.log("[Re:Telegram] " + text);
    }

    public static void log(Throwable throwable)
    {
        XposedBridge.log(throwable);
    }

    public static <T> ArrayList<T> castList(Object obj, Class<T> clazz)
    {
        ArrayList<T> result = new ArrayList<>();
        if (obj instanceof ArrayList<?>)
        {
            for (Object o : (ArrayList<?>) obj)
                result.add(clazz.cast(o));

            return result;
        }
        return null;
    }

    public static void readDeletedMessages()
    {
        if (deletedMessagesSavePath == null)
            return;

        try
        {
            JsonElement valueJsonElement = JsonParser.parseReader(new BufferedReader(new FileReader(deletedMessagesSavePath)));

            if (!valueJsonElement.isJsonNull() && valueJsonElement instanceof JsonObject) {
                JsonObject jsonObject = (JsonObject) valueJsonElement;

                jsonObject.entrySet().forEach(entry -> {
                    JsonArray jsonModule = entry.getValue().getAsJsonArray();
                    ArrayList<Integer> list = new ArrayList<>();
                    jsonModule.forEach(id -> list.add(id.getAsInt()));
                    AntiRecall.insertDeletedMessageFromSaveFile(Integer.parseInt(entry.getKey().trim()), list);
                });
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveDeletedMessages()
    {
        if (deletedMessagesSavePath == null)
            return;

        JsonObject valueJsonObject = new JsonObject();

        for (DeletedMessageInfo deletedMessageInfo : AntiRecall.getDeletedMessagesIds())
        {
            JsonArray jsonModule = new JsonArray();
            deletedMessageInfo.getMessageIds().forEach(jsonModule::add);
            valueJsonObject.add(String.valueOf(deletedMessageInfo.getSelectedAccount()), jsonModule);
        }

        FileUtils.save(deletedMessagesSavePath, BUILDER_GSON.toJson(valueJsonObject), false);
    }

    public static Gson getBuilderGson() {
        return BUILDER_GSON;
    }
}
