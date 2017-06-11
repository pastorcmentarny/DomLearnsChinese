package dms.pastor.chinesegame.learning.lessons;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.DomTimer;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Level;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;

import static dms.pastor.chinesegame.utils.UIUtils.setBackgroundColor;
import static dms.pastor.chinesegame.utils.UIUtils.setIncorrect;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;
import static dms.pastor.chinesegame.utils.UIUtils.setToDefault;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 02/01/2013
 */
public final class LessonTest extends Level implements View.OnClickListener {
    private static final String TAG = "LESSON TEST";
    @SuppressWarnings("FieldCanBeLocal")
    private final int fail = Config.DEFAULT_FAIL_POINTS;
    private SharedPreferences preferences;
    private Vibrator vibrator;
    private TableRow spellRow1;
    private TableRow spellRow2;
    private Word answerWord;
    private Word wrongWord1;
    private Word wrongWord2;
    private Word wrongWord3;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button showPinyinSpellButton;
    private TextView currentCharacter;
    private TextView currentPinyin;
    private ProgressBar levelProgressBar;
    private ArrayList<Word> words;
    private Player player;
    private int bonus = Config.DEFAULT_BONUS_POINTS;
    private boolean woops = false;

    private LinearLayout lifeMana;
    private TextView lvl;

    private TextView correctValue;
    private TextView mistakesValue;
    private final Runnable timerTicker = new Runnable() {
        public void run() {
            updateUI();
        }
    };
    private DomTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "creating activity");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.level);

        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);
        showPinyinSpellButton = (Button) findViewById(R.id.spell_show_pinyin_button);
        LinearLayout ll = (LinearLayout) findViewById(R.id.top_line);
        ll.setVisibility(View.GONE);

        lifeMana = (LinearLayout) findViewById(R.id.life_mana_ll);
        spellRow1 = (TableRow) findViewById(R.id.spell_row1);
        spellRow2 = (TableRow) findViewById(R.id.spell_row2);
        currentCharacter = (TextView) findViewById(R.id.currentCharacter);
        currentPinyin = (TextView) findViewById(R.id.currentPinyin);
        correctValue = (TextView) findViewById(R.id.correct_value);
        mistakesValue = (TextView) findViewById(R.id.mistakes_value);
        lvl = (TextView) findViewById(R.id.current_lvl);
        levelProgressBar = (ProgressBar) findViewById(R.id.level_progressbar);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        showPinyinSpellButton.setOnClickListener(this);

        timer = new DomTimer();

        setup();
        setupUI();
        setupTurn();
        updatePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (myTimer != null) {
            myTimer.cancel();
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }


    public void setupTurn() {
        Log.d(TAG, "setup turn");
        player.nextTurn();
        runTimer();
        timer.resetTimer();
        timer.start();
    }

    public void setup() {
        Log.d(TAG, "setup a lesson");
        player = Player.getPlayer();
        words = new ArrayList<>();
        try {
            answerWord = player.game.getGameWordsList().get(new Random().nextInt((player.game.getGameWordsList().size())));
            if (answerWord != null) {
                wrongWord1 = selectAWord(wrongWord1, new Word[]{answerWord});
                wrongWord2 = selectAWord(wrongWord2, new Word[]{answerWord, wrongWord1});
                wrongWord3 = selectAWord(wrongWord3, new Word[]{answerWord, wrongWord1, wrongWord2});

                words.add(answerWord);
                words.add(wrongWord1);
                words.add(wrongWord2);
                words.add(wrongWord3);
            } else {
                Toast.makeText(this, getString(R.string.woops), Toast.LENGTH_LONG).show();
                finish();
            }

        } catch (IllegalArgumentException iae) {
            Toast.makeText(this, getString(R.string.woops) + "(IAE)" + iae.getMessage() + "\nProblem with setting lesson test.", Toast.LENGTH_LONG).show();
            finish();
        } catch (NullPointerException npe) {
            Toast.makeText(this, getString(R.string.woops) + "(NPE)" + npe.getMessage() + "\nProblem with setting lesson test.", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void setupNewLevel() {
        woops = false;
        setEnabled(false);
        setup();
        setupUI();
        setupTurn();
        setEnabled(true);
        updatePlayer();
    }

    protected void setEnabled(boolean enabled) {
        answer1Button.setEnabled(enabled);
        answer2Button.setEnabled(enabled);
        answer3Button.setEnabled(enabled);
        answer4Button.setEnabled(enabled);
        showPinyinSpellButton.setEnabled(enabled);
    }

    private void checkAnswer(Button button) {
        if (super.isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {
            timer.stop();
            if (!woops) {
                player.game.addCorrect();
            }
            endOfLevel();
        } else {
            if (vibrator != null && preferences.getBoolean("vibrate", Config.DEFAULT_VIBRATE)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (preferences.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND)) {
                playTestTune(this);
            }
            setIncorrect(this, this, button);
            bonus -= fail;
            woops = true;
            player.game.addMistake();
            updatePlayer();
        }
    }

    public void setupUI() {
        setToDefault(this, answer1Button);
        setToDefault(this, answer2Button);
        setToDefault(this, answer3Button);
        setToDefault(this, answer4Button);
        Log.d(TAG, "creating UI...");
        if (preferences.getBoolean("lesson_pinyin", false)) {
            currentPinyin.setVisibility(TextView.VISIBLE);
        } else {
            currentPinyin.setVisibility(TextView.INVISIBLE);
        }
        lifeMana.setVisibility(View.GONE);
        spellRow1.setVisibility(View.GONE);
        spellRow2.setVisibility(View.GONE);
        levelProgressBar.setVisibility(View.VISIBLE);
        lvl.setText(String.valueOf(player.game.getLevel()));

        if (answerWord != null) {
            currentPinyin.setText(answerWord.getPinyin());
            currentCharacter.setText(answerWord.getChineseCharacter());
        } else {
            error("No answer word :(");
        }

        DomUtils.shuffle(words);
        answer1Button.setText(words.get(0).getWordInEnglish());
        answer2Button.setText(words.get(1).getWordInEnglish());
        answer3Button.setText(words.get(2).getWordInEnglish());
        answer4Button.setText(words.get(3).getWordInEnglish());

        TextView timeElapsedTitle = (TextView) findViewById(R.id.time_elasped_title);
        timeElapsedTitle.setVisibility(View.GONE);
        TextView timeElapsedValue = (TextView) findViewById(R.id.time_elasped_value);
        timeElapsedValue.setVisibility(View.GONE);

    }

    @SuppressWarnings("SameParameterValue")
    private void error(String msg) {
        Log.w(TAG, "WOOPS!\n" + msg);
        finish();
    }


    public void updatePlayer() {
        updateUI();
    }


    protected void updateUI() {
        correctValue.setText(String.valueOf(player.game.getCorrect()));
        mistakesValue.setText(String.valueOf(player.game.getMistake()));
        levelProgressBar.setProgress(player.game.getLevel());
        levelProgressBar.setMax(player.game.getLevels());
        answer1Button = checkIsButtonUsed(answer1Button);
        answer2Button = checkIsButtonUsed(answer2Button);
        answer3Button = checkIsButtonUsed(answer3Button);
        answer4Button = checkIsButtonUsed(answer4Button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.answer1:
                checkAnswer(answer1Button);
                break;
            case R.id.answer2:
                checkAnswer(answer2Button);
                break;
            case R.id.answer3:
                checkAnswer(answer3Button);
                break;
            case R.id.answer4:
                checkAnswer(answer4Button);
                break;
        }
    }


    public void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

    @Override
    public void endOfLevel() {
        Log.d(TAG, "end of level...");
        player.game.addLevel();

        if (player.game.isLastLevel()) {
            Intent ii = new Intent(this, LessonResult.class);
            startActivity(ii);
            finish();
        } else {
            setupNewLevel();
        }

    }

    private Button checkIsButtonUsed(Button button) {
        if (button.getText().toString().equalsIgnoreCase(getResources().getString(R.string.removed))) {
            button.setText(getResources().getString(R.string.removed));
            setTextColor(button, R.color.removed_button, this);
            setBackgroundColor(button, R.color.transparent, this);
            button.setEnabled(false);
        } else if (button.getText().toString().equalsIgnoreCase(getResources().getString(R.string.incorrect))) {
            button.setText(getResources().getString(R.string.incorrect));
            setTextColor(button, R.color.error, this);
            setBackgroundColor(button, R.color.transparent, this);
            button.setEnabled(false);
        }
        return button;
    }


}