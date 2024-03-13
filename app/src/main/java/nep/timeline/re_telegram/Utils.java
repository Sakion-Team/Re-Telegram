package nep.timeline.re_telegram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Utils {
    public static XC_LoadPackage.LoadPackageParam globalLoadPackageParam = null;
    public static final String issue = "Your telegram may have been modified! You can submit issue to let developer to try support to the telegram client you are using.";
    private static final Gson BUILDER_GSON = new GsonBuilder().setPrettyPrinting().create();
    //public static File deletedMessagesSavePath = null;
    public static File deletedMessagesDatabasePath = null;

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
        return result;
    }
/*
    public static void readDeletedMessages()
    {
        if (deletedMessagesSavePath == null)
            return;

        try
        {
            JsonElement valueJsonElement = JsonParser.parseReader(new BufferedReader(new FileReader(deletedMessagesSavePath)));

            if (!valueJsonElement.isJsonNull() && valueJsonElement instanceof JsonObject jsonObject) {
                jsonObject.entrySet().forEach(entry -> {
                    JsonObject jsonObject2 = entry.getValue().getAsJsonObject();
                    jsonObject2.entrySet().forEach(entry2 -> {
                        JsonArray jsonModule = entry2.getValue().getAsJsonArray();
                        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
                        jsonModule.forEach(id -> list.add(id.getAsInt()));
                        AntiRecall.insertDeletedMessageFromSaveFile(Integer.parseInt(entry.getKey().trim()), Long.parseLong(entry2.getKey().trim()), list);
                    });
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

        List<Integer> accounts = new ArrayList<>();

        for (DeletedMessageInfo deletedMessageInfo : AntiRecall.getDeletedMessagesIds())
        {
            if (!accounts.contains(deletedMessageInfo.getSelectedAccount()))
                accounts.add(deletedMessageInfo.getSelectedAccount());
        }

        if (!accounts.isEmpty())
        {
            for (int account : accounts) {
                JsonObject valueJsonObject2 = new JsonObject();

                for (DeletedMessageInfo deletedMessageInfo : AntiRecall.getDeletedMessagesIds()) {
                    if (deletedMessageInfo.getSelectedAccount() == account)
                    {
                        JsonArray jsonModule = new JsonArray();
                        deletedMessageInfo.getMessageIds().forEach(jsonModule::add);
                        valueJsonObject2.add(String.valueOf(deletedMessageInfo.getChannelID()), jsonModule);
                    }
                }

                valueJsonObject.add(String.valueOf(account), valueJsonObject2);
            }
        }

        FileUtils.save(deletedMessagesSavePath, BUILDER_GSON.toJson(valueJsonObject), false);
    }
*/
    public static Gson getBuilderGson() {
        return BUILDER_GSON;
    }
}
