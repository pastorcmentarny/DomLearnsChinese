package dms.pastor.chinesegame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dms.pastor.chinesegame.AppLauncher;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.dictionary.Word;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 07/01/2013
 */
public final class DomUtils {

    private static final String TAG = "Dom Utils";

    private DomUtils() {
        //Utility classes should not have a public or default constructor.
    }

    public static int getResultIn0to100Range(int result) {
        if (result > 100) {
            result = 100;
        }
        if (result < 0) {
            result = 0;
        }
        return result;
    }

    public static String getAppVersion(Context context) {
        try {
            return "v" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName +
                    "(" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + ")";
        } catch (NameNotFoundException ntfe) {
            return "Unknown";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException ntfe) {
            Log.w(TAG, "NameNotFoundException" + ntfe.getMessage());
            return 0;
        } catch (Exception e) {
            Log.w(TAG, "NameNotFoundException" + e.getMessage());
            return 0;
        }
    }

    public static void displayError(Context context, String error) {
        Toast.makeText(context, context.getString(R.string.e) + "\n\n" + error + context.getString(R.string.e_inform_me), LENGTH_LONG).show();
    }

    public static void displayWandToast(Context context, Activity activity, String text, boolean extendedDisplay, boolean good) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View myLayout = layoutInflater.inflate(R.layout.domtoast, (ViewGroup) activity.findViewById(R.id.toast_layout));
        ImageView myImage = (ImageView) myLayout.findViewById(R.id.img);
        myImage.setImageResource(R.drawable.wand);
        TextView myMessage = (TextView) myLayout.findViewById(R.id.text_to_display);
        myMessage.setText(text);
        if (good) {
            UIUtils.setTextColor(myMessage, R.color.good_news, activity);
        } else {
            UIUtils.setTextColor(myMessage, R.color.bad_news, activity);
        }
        Toast myToast = new Toast(context.getApplicationContext());
        if (extendedDisplay) {
            myToast.setDuration(LENGTH_LONG);
        } else {
            myToast.setDuration(LENGTH_SHORT);
        }
        myToast.setView(myLayout);
        myToast.show();
    }

    public static void msg(Context context, String msg) {
        Toast.makeText(context, msg, LENGTH_SHORT).show();
    }

    public static void sorryToast(Context context) {
        Toast.makeText(context, context.getString(R.string.sorry_msg), LENGTH_SHORT).show();
    }

    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String getUnknownWhenNullString(String string) {
        return string != null ? string : "UNKNOWN";
    }

    public static String getResultTimeAsString(long time) {
        int sec = (int) time / 1000;
        int dot = (int) (time % 1000) / 100;
        return String.format("%s.%ss.", String.valueOf(sec), String.valueOf(dot));
    }

    public static ArrayList<Word> shuffle(ArrayList<Word> words) {
        Log.d(TAG, "shuffling words");
        int number = words.size();

        for (int i = 0; i < number; i++) {
            int r = i + (int) (Math.random() * (number - i));
            Word swap = words.get(r);
            words.set(r, words.get(i));
            words.set(i, swap);
        }
        return words;
    }

    public static ArrayList<Button> shuffleButtons(ArrayList<Button> buttons) {
        Log.d(TAG, "shuffling words");
        int size = buttons.size();

        for (int i = 0; i < size; i++) {
            int r = i + (int) (Math.random() * (size - i));
            Button swap = buttons.get(r);
            buttons.set(r, buttons.get(i));
            buttons.set(i, swap);
        }

        return buttons;
    }


    public static boolean isStringNotEmpty(String cmd) {
        return !isStringEmpty(cmd);
    }

    public static void goToHome(Context context, Activity currentActivity) {
        Intent inMain = new Intent(currentActivity, AppLauncher.class);
        inMain.setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(inMain);
    }

    public static int parseIntNullSafe(String value, int unknownValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return unknownValue;
        }
    }

    @SuppressWarnings("SameParameterValue")
    public static long parseLongNullSafe(String value, long unknownValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return unknownValue;
        }
    }

    public static void copyXItemsFromOneListToAnother(int number, ArrayList<Word> wordList, ArrayList<Word> gameWordsList) {
        if (number > wordList.size()) {
            Log.wtf(TAG, "Bug detected! size of wordList is bigger than number that should be added.");
        }
        for (int i = 0; i < number; i++) {
            gameWordsList.add(wordList.get(i));
        }
    }

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, LENGTH_SHORT).show();
    }
}