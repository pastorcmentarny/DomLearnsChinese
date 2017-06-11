package dms.pastor.chinesegame.extras;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.WordContainer;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.extras.util.SystemUiHider;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 */
public final class RandomWordActivity extends Activity implements View.OnClickListener, Runnable {
    public static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private static final String TAG = "Random";
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    private static final boolean TOGGLE_ON_CLICK = true;
    private final Handler mHideHandler = new Handler();
    private TextView chinese;
    private TextView pinyin;
    private TextView englishTextView;
    private TextView polishTextView;
    private TextView sentenceTextView;
    private TextView notesTextView;
    private WordContainer wordContainer;
    private SharedPreferences settings;
    private final Runnable timerTicker = new Runnable() {
        public void run() {
            nextWord();
        }
    };
    private Timer myTimer;
    private SystemUiHider mSystemUiHider;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_random_word);
        final View contentView = findViewById(R.id.fullscreen_content);
        chinese = (TextView) findViewById(R.id.character);
        pinyin = (TextView) findViewById(R.id.pinyinTextView);
        englishTextView = (TextView) findViewById(R.id.englishTextView);
        polishTextView = (TextView) findViewById(R.id.polishTextView);
        notesTextView = (TextView) findViewById(R.id.notesTextView);
        sentenceTextView = (TextView) findViewById(R.id.sentenceTextView);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        Button copyButton = (Button) findViewById(R.id.copyButton);
        copyButton.setOnClickListener(this);
        wordContainer = WordContainer.getWordContainer();
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        mSystemUiHider = SystemUiHider.getInstance(this, contentView);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (mShortAnimTime == 0) {
                            mShortAnimTime = getResources().getInteger(
                                    android.R.integer.config_shortAnimTime);
                        }
                        if (visible && AUTO_HIDE) {
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
        wordContainer.getCurrentWord();
    }

    @Override
    protected void onPause() {
        myTimer.cancel();
        super.onPause();
    }

    private void setup() {
        wordContainer = WordContainer.getWordContainer();
        runTimer();
    }

    private void nextWord() {
        wordContainer.nextRandomWord();
        updateUI();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                nextWord();
                break;
            case R.id.copyButton:
                Clipboard4 clipboard = new Clipboard4();
                String result;
                if (clipboard.saveText(this, wordContainer.getCurrentWord().toString())) {
                    result = "Saved to clipboard";
                } else {
                    result = " Unable to save to clipboard :(";
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.w(TAG, "Unsupported action for button with id:" + v.getId());
        }
    }

    @Override
    public void run() {
    }

    private void updateUI() {
        chinese.setText(wordContainer.getCurrentWord().getChineseCharacter());
        pinyin.setText(wordContainer.getCurrentWord().getPinyin());
        englishTextView.setText(wordContainer.getCurrentWord().getWordInEnglish());
        if (settings.getBoolean("polishMode", false)) {
            polishTextView.setText(wordContainer.getCurrentWord().getWordInPolish());
            polishTextView.setVisibility(View.VISIBLE);
        }
        notesTextView.setText(wordContainer.getCurrentWord().getNotes());
        sentenceTextView.setText(R.string.not_available);
    }

    private void runTimer() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }

        }, 0, Config.NEXT_WORD_TIME);
    }

    private void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

}
