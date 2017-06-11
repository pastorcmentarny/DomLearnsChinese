package dms.pastor.chinesegame.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import dms.pastor.chinesegame.Config;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 15/04/2014
 */
public final class FileUtils {
    private static final String TAG = "File Utils";

    private FileUtils() {
    }

    public static boolean saveTextToFile(String content, File file) {

        OutputStreamWriter out = null;
        try {
            FileOutputStream os = new FileOutputStream(file);

            out = new OutputStreamWriter(os);
            out.write(content);
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

        StringBuilder text = new StringBuilder(Config.EMPTY_STRING);
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
