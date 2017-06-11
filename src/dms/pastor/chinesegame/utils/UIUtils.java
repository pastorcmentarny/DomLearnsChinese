package dms.pastor.chinesegame.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import dms.pastor.chinesegame.R;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.widget.Toast.LENGTH_LONG;
import static dms.pastor.chinesegame.utils.Utils.getAdRequest;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * Email: email can be found on my website
 * <p>
 * Created 03.12.2012
 */
public final class UIUtils {

    private static final String EMPTY_STRING = "";
    private static final String OK = "OK";
    private static final String CANCEL = "CANCEL";

    private UIUtils() {
    }
    public static void setIncorrect(Activity activity, Context context, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.remove_bad_answer);
        button.startAnimation(animWrong);
        button.setText(context.getResources().getString(R.string.incorrect));
        UIUtils.setTextColor(button, R.color.error, activity);
        UIUtils.setBackgroundColor(button, R.color.transparent, activity);
        button.setEnabled(false);
    }

    public static void setRemoved(Activity activity, Context context, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.remove_bad_answer);
        button.startAnimation(animWrong);
        button.setText(context.getResources().getString(R.string.removed));
        UIUtils.setTextColor(button, R.color.removed_button, activity);
        UIUtils.setBackgroundColor(button, android.R.color.black, activity);
        button.setBackgroundColor(Color.BLACK);
        button.setEnabled(false);
    }

    @SuppressWarnings("deprecation")
    public static void setUsed(Context context, Button button) {
        button.setEnabled(false);
        button.setTextColor(Color.DKGRAY);
        if (SDK_INT >= 16) {
            button.setBackground(context.getResources().getDrawable(R.drawable.spell_button));
        } else {
            button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.spell_used_button));
        }
    }

    @SuppressWarnings("deprecation")
    public static void setButtonEnabled(Context context, Button button, boolean enabled) {
        button.setEnabled(enabled);
        if (SDK_INT >= 16) {
            if (enabled) {
                button.setBackground(context.getResources().getDrawable(R.drawable.spell_button));
            } else {
                button.setBackground(context.getResources().getDrawable(R.drawable.spell_disabled_button));
            }
        } else {
            if (enabled) {
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.spell_button));
            } else {
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.spell_disabled_button));
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void setTextColor(TextView view, int color, Activity activity) {
        if (SDK_INT >= VERSION_CODES.M) {
            view.setTextColor(activity.getResources().getColor(color, activity.getTheme()));
        } else {
            view.setTextColor(activity.getResources().getColor(color));
        }
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundColor(View view, int color, Activity activity) {
        if (SDK_INT >= VERSION_CODES.M) {
            view.setBackgroundColor(activity.getResources().getColor(color, activity.getTheme()));
        } else {
            view.setBackgroundColor(activity.getResources().getColor(color));
        }
    }

    public static void newRecord(Context context, TextView text, Activity activity) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.new_record);
        text.startAnimation(animWrong);
        text.setText(context.getResources().getString(R.string.new_record));
        UIUtils.setTextColor(text, R.color.good_news, activity);
        text.setTypeface(null, Typeface.BOLD);
        UIUtils.setBackgroundColor(text, R.color.error, activity);
        text.setEnabled(false);
    }

    public static void displayError(Context context, String error) {
        Toast.makeText(context, context.getString(R.string.e) + "\n\n" + error + context.getString(R.string.e_inform_me), LENGTH_LONG).show();
    }

    public static void setUnTapButton(Context context, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.untap_anim);
        button.startAnimation(animWrong);
        button.setEnabled(true);
        button.setText(context.getString(R.string.unfreeze));
        UIUtils.setBackground(button, Utils.getDrawable(context, R.drawable.unfreeze_button));
    }

    public static void setFrozen(Context context, Button button, Activity activity) {
        button.setText(context.getString(R.string.frozen));
        UIUtils.setTextColor(button, R.color.freeze_text, activity);
        UIUtils.setBackgroundColor(button, R.color.freeze_text_background, activity);
        button.setEnabled(false);
    }

    public static void setInvisibleButton(Activity activity, Button button) {
        UIUtils.setTextColor(button, R.color.transparent, activity);
        UIUtils.setBackgroundColor(button, R.color.transparent, activity);
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(Button button, Drawable background) {
        if (SDK_INT >= 16) {
            button.setBackground(background);
        } else {
            button.setBackgroundDrawable(background);
        }
    }

    public static void setAllToDefault(Activity activity, Button answer1Button, Button answer2Button, Button answer3Button, Button answer4Button) {
        setToDefault(activity, answer1Button);
        setToDefault(activity, answer2Button);
        setToDefault(activity, answer3Button);
        setToDefault(activity, answer4Button);
    }

    public static void setToDefault(Activity activity, Button button) {
        button.setBackgroundResource(android.R.drawable.btn_default);
        UIUtils.setTextColor(button, android.R.color.black, activity);
    }

    public static void displayHalfHalfToast(Context context, Activity activity) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View myLayout = layoutInflater.inflate(R.layout.domtoast, (ViewGroup) activity.findViewById(R.id.toastlayout));
        ImageView myImage = (ImageView) myLayout.findViewById(R.id.img);
        myImage.setImageResource(R.drawable.half);
        TextView myMessage = (TextView) myLayout.findViewById(R.id.txtvdisplay);
        myMessage.setText(context.getString(R.string.halfHalfToGuess));
        showToast(context, myLayout);
    }

    public static ArrayList<Button> shuffleButtons(ArrayList<Button> buttons) {
        int size = buttons.size();

        for (int i = 0; i < size; i++) {
            int r = i + (int) (Math.random() * (size - i));
            Button swap = buttons.get(r);
            buttons.set(r, buttons.get(i));
            buttons.set(i, swap);
        }

        return buttons;
    }

    public static void usernameDialog(final Context context) {
        Builder alert = new Builder(context);

        alert.setTitle(context.getString(R.string.username_title));
        alert.setMessage(context.getString(R.string.username_question));

        InputFilter filter = getInputFilter();

        final EditText input = new EditText(context);
        input.setFilters(new InputFilter[]{filter});
        alert.setView(input);

        alert.setPositiveButton(OK, getPositiveButtonOnClickListener(context, input));
        alert.setNegativeButton(CANCEL, getNegativeButtonOnClickListener());
        alert.show();
    }

    @NonNull
    private static OnClickListener getPositiveButtonOnClickListener(final Context context, final EditText input) {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username = input.getText().toString();
                if (!DomUtils.isStringEmpty(username)) {
                    saveUserName(username, context);
                }

            }
        };
    }

    @NonNull
    private static OnClickListener getNegativeButtonOnClickListener() {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("usernameDialog", "Username not set.");
            }
        };
    }

    @NonNull
    private static InputFilter getInputFilter() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int destinationStart, int destinationEnd) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                        return EMPTY_STRING;
                    }
                }
                return null;    //It is null ,otherwise it will not work..
            }
        };
    }

    private static void saveUserName(String username, Context context) {
        SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putString("username", username);
        preferencesEditor.apply();
    }

    private static void showToast(Context context, View myLayout) {
        Toast myToast = new Toast(context.getApplicationContext());
        myToast.setDuration(LENGTH_LONG);
        myToast.setView(myLayout);
        myToast.show();
    }

    public static void loadAd(Activity activity, Context context) {
        AdView adView = (AdView) activity.findViewById(R.id.adView);
        try {
            AdRequest adRequest = getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(context.getString(R.string.am_e), context.getString(R.string.am_e_init) + e.getMessage());
        }
    }

}
