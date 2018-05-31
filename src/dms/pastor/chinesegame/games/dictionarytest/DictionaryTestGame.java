package dms.pastor.chinesegame.games.dictionarytest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.DomTimer;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Level;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 24/11/2012
 */
public final class DictionaryTestGame extends Level implements View.OnClickListener {
    private static final String TAG = "Dictionary Test Game";
    private Button showPinyinButton;
    private SharedPreferences prefs, settings;
    private Statistic statistic;

    private Vibrator vibrator;
    private TableRow spellRow1;
    private TableRow spellRow2;
    private TableRow spellRow4;

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
    private Player player;
    private boolean woops = false;
    private boolean pinyinUsed = false;
    private LinearLayout lifeMana;
    private Button skipButton;
    private TextView lvl;
    private TextView timeElapsed;
    private TextView correctValue;
    private TextView mistakesValue;
    private TextView status;
    private TextView skippedValue;
    private DomTimer timer;
    private TextView currentScore;
    private final Runnable timerTicker = this::updateUI;
    private TextView bonusScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.level);

        prefs = PreferenceManager.getDefaultSharedPreferences(DictionaryTestGame.this);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        statistic = Statistic.getStatistic(this);

        answer1Button = findViewById(R.id.answer1);
        answer2Button = findViewById(R.id.answer2);
        answer3Button = findViewById(R.id.answer3);
        answer4Button = findViewById(R.id.answer4);


        lifeMana = findViewById(R.id.life_mana_ll);
        LinearLayout timeLayout = findViewById(R.id.time_layout);
        timeLayout.setVisibility(View.GONE);

        currentScore = findViewById(R.id.current_score);
        bonusScore = findViewById(R.id.bonus_score);

        spellRow1 = findViewById(R.id.spell_row1);
        spellRow2 = findViewById(R.id.spell_row2);
        spellRow4 = findViewById(R.id.spell_row4);
        spellRow4.setVisibility(View.VISIBLE);

        TableRow skipRow = findViewById(R.id.spell_row3);
        skipRow.setVisibility(View.VISIBLE);
        skipButton = findViewById(R.id.tap2unfreeze);
        skipButton.setOnClickListener(this);
        skipButton.setVisibility(View.VISIBLE);
        skipButton.setText(R.string.skip_answer);

        currentCharacter = findViewById(R.id.currentCharacter);
        currentPinyin = findViewById(R.id.currentPinyin);
        timeElapsed = findViewById(R.id.time_elapsed_value);
        correctValue = findViewById(R.id.correct_value);
        mistakesValue = findViewById(R.id.mistakes_value);

        levelProgressBar = findViewById(R.id.level_progressbar);
        status = findViewById(R.id.levelStatus);

        TextView skippedSeparator = findViewById(R.id.skipped_separator);
        TextView skippedTitle = findViewById(R.id.skipped_title);
        skippedValue = findViewById(R.id.skipped_value);
        skippedSeparator.setVisibility(View.VISIBLE);
        skippedTitle.setVisibility(View.VISIBLE);
        skippedValue.setVisibility(View.VISIBLE);

        lvl = findViewById(R.id.current_lvl);

        showPinyinButton = findViewById(R.id.showPinyin);
        showPinyinButton.setOnClickListener(this);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        showPinyinButton.setOnClickListener(this);

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
        pinyinUsed = false;
        setEnabled(true);
        runTimer();
        timer.resetTimer();
        timer.start();
    }

    @Override
    public void setup() {
        player = Player.getPlayer();
        words = new ArrayList<>();
        answerWord = player.getGame().getAnswerWordsForLevels().get(player.getGame().getLevel() - 1);
        player.getGame().setGameWordList(Dictionary.getDictionary().getWordsForLevel(answerWord.getDifficulty()));
        wrongWord1 = selectAWord(wrongWord1, new Word[]{answerWord});
        wrongWord2 = selectAWord(wrongWord2, new Word[]{answerWord, wrongWord1});
        wrongWord3 = selectAWord(wrongWord3, new Word[]{answerWord, wrongWord1, wrongWord2});

        words.add(answerWord);
        words.add(wrongWord1);
        words.add(wrongWord2);
        words.add(wrongWord3);


        setupTurn();
        setupUI();
        updatePlayer();
    }

    private void checkAnswer(Button button) {
        if (super.isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {
            timer.stop();
            setEnabled(false);
            if (!woops) {
                player.getGame().addCorrect();
                if (pinyinUsed) {
                    int reducedScore = DictionaryLevelInfo.getScoreForLevel(answerWord.getDifficulty()) / 3;
                    if (reducedScore < 1) {
                        reducedScore = 1;
                    }
                    Log.i(TAG, "reducedScore: " + reducedScore);
                    if (reducedScore <= answerWord.getDifficulty()) {
                        reducedScore = answerWord.getDifficulty();
                    }
                    player.addScore(reducedScore);
                } else {
                    player.addScore(DictionaryLevelInfo.getScoreForLevel(answerWord.getDifficulty()));
                }

                updateStatus(String.format(Locale.ENGLISH, "You answered correctly. Word difficulty: %d", answerWord.getDifficulty()), R.color.green);
            }
            endOfLevel();
        } else {
            if (vibrator != null && prefs.getBoolean("vibrate", Config.DEFAULT_VIBRATE)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (prefs.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND)) {
                playTestTune(this);
            }
            UIUtils.setIncorrect(this, this, button);
            setVisibility(false, ButtonType.OTHERS);
            if (woops) {
                player.addScore((-1) * player.getGame().getMistake());
                updateStatus(String.format(Locale.ENGLISH, "Another wrong answer.Total mistakes so far: %d", player.getGame().getMistake()), R.color.error);
            } else {
                player.addScore((-1) * DictionaryLevelInfo.getPenaltyForLevel(answerWord.getDifficulty()));
                updateStatus("Wrong answer", R.color.error);
            }
            woops = true;
            player.getGame().addMistake();
            updatePlayer();
        }
    }

    private void setVisibility(boolean buttonsVisible, ButtonType buttonType) {
        if (buttonType.equals(ButtonType.ANSWERS)) {
            if (buttonsVisible) {
                answer1Button.setVisibility(View.VISIBLE);
                answer2Button.setVisibility(View.VISIBLE);
                answer3Button.setVisibility(View.VISIBLE);
                answer4Button.setVisibility(View.VISIBLE);
            } else {
                answer1Button.setVisibility(View.INVISIBLE);
                answer2Button.setVisibility(View.INVISIBLE);
                answer3Button.setVisibility(View.INVISIBLE);
                answer4Button.setVisibility(View.INVISIBLE);
            }
        } else {
            if (buttonsVisible) {
                skipButton.setVisibility(View.VISIBLE);
                showPinyinButton.setVisibility(View.VISIBLE);
            } else {
                skipButton.setVisibility(View.INVISIBLE);
                showPinyinButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateStatus(String message, int color) {
        status.setText(message);
        setTextColor(status, color, this);
    }

    public void setupUI() {
        if (settings.getBoolean("hsk_pinyin", false)) {
            currentPinyin.setVisibility(TextView.VISIBLE);
        } else {
            currentPinyin.setVisibility(TextView.INVISIBLE);
        }
        lifeMana.setVisibility(View.GONE);
        spellRow1.setVisibility(View.GONE);
        spellRow2.setVisibility(View.GONE);
        spellRow4.setVisibility(View.VISIBLE);
        levelProgressBar.setVisibility(View.VISIBLE);
        setVisibility(true, ButtonType.ANSWERS);
        setVisibility(true, ButtonType.OTHERS);
        lvl.setText(String.valueOf(player.getGame().getLevel()));
        currentPinyin.setText(answerWord.getPinyin());
        currentCharacter.setText(answerWord.getChineseCharacter());

        bonusScore.setVisibility(View.GONE);


        DomUtils.shuffle(words);
        setButtonToDefault(answer1Button, 0);
        setButtonToDefault(answer2Button, 1);
        setButtonToDefault(answer3Button, 2);
        setButtonToDefault(answer4Button, 3);
        showPinyinButton.setTextColor(Color.WHITE);
        skipButton.setTextColor(Color.WHITE);
        setEnabled(true);
        setVisibility(true, ButtonType.ANSWERS);
        setVisibility(true, ButtonType.OTHERS);

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
        showPinyinButton.setEnabled(enabled);
        skipButton.setEnabled(enabled);
    }

    public void updatePlayer() {
        updateUI();
    }

    protected void updateUI() {
        timeElapsed.setText(DomUtils.getResultTimeAsString(player.getGame().getCurrentTime()));
        correctValue.setText(String.valueOf(player.getGame().getCorrect()));
        mistakesValue.setText(String.valueOf(player.getGame().getMistake()));
        skippedValue.setText(String.valueOf(player.getGame().getSkipped()));
        levelProgressBar.setProgress(player.getGame().getLevel());
        levelProgressBar.setMax(player.getGame().getLevels());
        currentScore.setText(String.valueOf(player.getScore()));
        if (pinyinUsed) {
            UIUtils.setUsed(this, showPinyinButton);
            UIUtils.setUsed(this, skipButton);
        }
    }

    @Override
    public void onClick(View view) {
        final Animation castSpell = AnimationUtils.loadAnimation(this, R.anim.cast_spell);
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
            case R.id.tap2unfreeze:
                view.startAnimation(castSpell);
                Log.d(TAG, "Player skipped an answer");
                timer.stop();
                setEnabled(false);
                player.getGame().addSkipped();
                player.addScore(-1);
                statistic.addSkipped();
                updateStatus(String.format(Locale.ENGLISH, "You skipped this question.Total skipped answer so far: %d", player.getGame().getSkipped()), R.color.info);
                endOfLevel();
                break;
            case R.id.showPinyin:
                view.startAnimation(castSpell);
                statistic.addPinyinUsed();
                status.setText(getString(R.string.cast_show_pinyin));
                UIUtils.setTextColor(status, R.color.good_news, this);
                currentPinyin.setVisibility(TextView.VISIBLE);
                setVisibility(false, ButtonType.OTHERS);
                pinyinUsed = true;
                updatePlayer();
                break;
            default:
                Log.e(TAG, "bug detected! Not implemented button in Dictionary Test Game");
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
            ii = new Intent(this, DictionaryTestResult.class);
            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ii);
            finish();

        } else {
            setup();
        }
    }

    private enum ButtonType {
        ANSWERS,
        OTHERS,
    }

}
