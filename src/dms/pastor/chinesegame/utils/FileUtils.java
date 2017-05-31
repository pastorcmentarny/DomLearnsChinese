package dms.pastor.chinesegame.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author Pastor Cmentarny
 *         Created 2014-05-15.
 */
public final class FileUtils {
    private static final String TAG = "File Utils";

    private FileUtils() {
        //Utility classes should not have a public or default constructor.
    }

    public static boolean saveTextToFile(String content, File file) {

        OutputStreamWriter out = null;
        try {
            FileOutputStream os = new FileOutputStream(file);

            out = new OutputStreamWriter(os);
            out.write(content);
            out.close();

        } catch (IOException e) {
            Log.w(TAG, "ExternalStorage. Error writing " + file, e);
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.w(TAG, "FileUtils/saveError occurred while closing FileOutputStream  " + file, e);
                }
            }
        }
        return true;
    }

    public static String loadTextFromFile(File file) {

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            Log.w(TAG, "load stats.IOException" + e.getMessage(), e);

        }
        return text.toString();
    }

}
