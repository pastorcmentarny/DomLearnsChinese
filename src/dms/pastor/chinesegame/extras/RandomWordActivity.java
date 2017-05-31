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

public final class RandomWordActivity extends Activity implements View.OnClickListener, Runnable {
    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    public static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private static final String TAG = "Random";
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
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
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
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

        //Statistic statistic = Statistic.getStatistic(this);
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

                        //noinspection ConstantConditions,PointlessBooleanExpression
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
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

        //randomWord.setOnTouchListener(mDelayHideTouchListener);
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

/*
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

        }
    };
 */

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
