package dms.pastor.chinesegame.games.hsk.basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.DomTimer;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Level;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.utils.UIUtils.setIncorrect;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 24/11/2012
 */
public final class HSKLevel extends Level implements View.OnClickListener {

    private SharedPreferences prefs, settings;

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

    private TextView currentCharacter;
    private TextView currentPinyin;
    private ProgressBar levelProgressBar;
    private ArrayList<Word> words;
    private Timer myTimer;
    private Player player;
    private int bonus = Config.DEFAULT_BONUS_POINTS;
    private boolean woops = false;
    private LinearLayout timeLayout;
    private LinearLayout lifeMana;
    private TextView lvl;
    private TextView timeLeft;
    private TextView timeElapsed;
    private TextView correctValue;
    private TextView mistakesValue;
    private final Runnable timerTicker = this::updateUI;
    private DomTimer timer;
    private TextView scoreTitle, currentScore, bonusScore, scoreSeparator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.level);

        prefs = PreferenceManager.getDefaultSharedPreferences(HSKLevel.this);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        answer1Button = findViewById(R.id.answer1);
        answer2Button = findViewById(R.id.answer2);
        answer3Button = findViewById(R.id.answer3);
        answer4Button = findViewById(R.id.answer4);

        lifeMana = findViewById(R.id.life_mana_ll);
        timeLayout = findViewById(R.id.time_layout);

        scoreTitle = findViewById(R.id.score_title);
        currentScore = findViewById(R.id.current_score);
        bonusScore = findViewById(R.id.bonus_score);
        scoreSeparator = findViewById(R.id.score_separator);


        spellRow1 = findViewById(R.id.spell_row1);
        spellRow2 = findViewById(R.id.spell_row2);

        currentCharacter = findViewById(R.id.currentCharacter);
        currentPinyin = findViewById(R.id.currentPinyin);
        timeLeft = findViewById(R.id.time_left_value);
        timeElapsed = findViewById(R.id.time_elapsed_value);
        correctValue = findViewById(R.id.correct_value);
        mistakesValue = findViewById(R.id.mistakes_value);


        levelProgressBar = findViewById(R.id.level_progressbar);


        lvl = findViewById(R.id.current_lvl);
        Button showPinyinSpellButton = findViewById(R.id.spell_show_pinyin_button);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        showPinyinSpellButton.setOnClickListener(this);

        timer = new DomTimer();

        setup();
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

    @Override
    public void setupTurn() {
        player.nextTurn();
        woops = false;
        runTimer();
        timer.resetTimer();
        timer.start();
    }


    @Override
    public void setup() {
        player = Player.getPlayer();
        words = new ArrayList<>();
        answerWord = player.getGame().getAnswerWordsForLevels().get(player.getGame().getLevel());
        wrongWord1 = selectAWord(wrongWord1, new Word[]{answerWord});
        wrongWord2 = selectAWord(wrongWord2, new Word[]{answerWord, wrongWord1});
        wrongWord3 = selectAWord(wrongWord3, new Word[]{answerWord, wrongWord1, wrongWord2});

        words.add(answerWord);
        words.add(wrongWord1);
        words.add(wrongWord2);
        words.add(wrongWord3);
        setupUI();
        setupTurn();
        updatePlayer();
    }


    private void checkAnswer(Button button) {
        if (isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {
            timer.stop();
            if (!woops) {
                player.getGame().addCorrect();
            }
            endOfLevel();
        } else {
            if (vibrator != null && prefs.getBoolean("vibrate", Config.DEFAULT_VIBRATE)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (prefs.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND)) {
                playTestTune(this);
            }
            setIncorrect(this, this, button);
            int fail = Config.DEFAULT_FAIL_POINTS;
            bonus -= fail;
            woops = true;
            player.getGame().addMistake();
            updatePlayer();
        }
    }


    public void setupUI() {
        if (settings.getBoolean("hsk_pinyin", false)) {
            currentPinyin.setVisibility(TextView.VISIBLE);
        } else {
            currentPinyin.setVisibility(TextView.INVISIBLE);
        }
        timeLayout.setVisibility(View.VISIBLE);
        lifeMana.setVisibility(View.GONE);
        spellRow1.setVisibility(View.GONE);
        spellRow2.setVisibility(View.GONE);
        levelProgressBar.setVisibility(View.VISIBLE);
        lvl.setText(String.valueOf(player.getGame().getLevel()));
        currentPinyin.setText(answerWord.getPinyin());
        currentCharacter.setText(answerWord.getChineseCharacter());

        scoreTitle.setVisibility(View.GONE);
        currentScore.setVisibility(View.GONE);
        bonusScore.setVisibility(View.GONE);
        scoreSeparator.setVisibility(View.GONE);

        DomUtils.shuffle(words);
        setButtonToDefault(answer1Button, 0);
        setButtonToDefault(answer2Button, 1);
        setButtonToDefault(answer3Button, 2);
        setButtonToDefault(answer4Button, 3);
        setEnabled(true);

    }


    private void setButtonToDefault(Button button, int wi) {
        button.setText(words.get(wi).getWordInEnglish());
        button.setBackgroundResource(android.R.drawable.btn_default);
        UIUtils.setTextColor(button, android.R.color.black, this);
    }

    protected void setEnabled(boolean enabled) {
        answer1Button.setEnabled(enabled);
        answer2Button.setEnabled(enabled);
        answer3Button.setEnabled(enabled);
        answer4Button.setEnabled(enabled);
    }


    public void updatePlayer() {
        updateUI();
    }


    protected void updateUI() {
        timeElapsed.setText(DomUtils.getResultTimeAsString(player.getGame().getCurrentTime()));
        timeLeft.setText(String.valueOf(((Config.HSK_BASIC_TIME_LIMIT / 1000 - player.getGame().getCurrentTimeInSeconds()))));
        correctValue.setText(String.valueOf(player.getGame().getCorrect()));
        mistakesValue.setText(String.valueOf(player.getGame().getMistake()));
        levelProgressBar.setProgress(player.getGame().getLevel());
        levelProgressBar.setMax(player.getGame().getLevels());
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


    protected void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

    @Override
    public void endOfLevel() {
        player.getGame().addToTotalTime(timer.calcTotalTime());
        player.getGame().addLevel();
        Intent ii;
        if (player.getGame().isLastLevel()) {
            player.getGame().timeStop();
            player.getGame().setTotalTime(player.getGame().getStopTime() - player.getGame().getStartTime());
            ii = new Intent(this, HSKResult.class);
            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
            finish();

        } else {
            setup();
        }
    }

    protected void runTimer() {
        myTimer = new Timer();
        myTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        timerMethod();
                    }

                }, 0, Config.SECONDS);
    }

}
