package nep.timeline.re_telegram.utils;

import java.io.*;

import de.robv.android.xposed.XposedBridge;
import nep.timeline.re_telegram.Utils;

public class FileUtils {
    public static String read(File file)
    {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
            return stringBuffer.toString();
        } catch (IOException e) {
            Utils.log(e);
            e.printStackTrace();
        }

        return "";
    }

    public static boolean save(File file, String content, boolean append)
    {
        try {
            if (!file.exists() && !file.createNewFile())
            {
                Utils.log("Cannot create file " + file.getAbsoluteFile());
                return false;
            }

            try (FileWriter writer = new FileWriter(file, append)) {
                writer.write(content);
            }

            return true;
        } catch (IOException e) {
            Utils.log(e);
            e.printStackTrace();
        }

        return false;
    }
}
