package dms.pastor.chinesegame.games.survival.saper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.DomTimer;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Level;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.games.calculator.SapperScoreCalculator;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.VISIBLE;
import static android.view.Window.FEATURE_NO_TITLE;
import static dms.pastor.chinesegame.Config.HEALTH_BONUS_PER_LEVEL;
import static dms.pastor.chinesegame.Config.MANA_BONUS_PER_LEVEL;
import static dms.pastor.chinesegame.Config.SAPPER_NO_PENALTY_TIME;
import static dms.pastor.chinesegame.Config.SECONDS;
import static dms.pastor.chinesegame.common.enums.Stage.getDifficultyLevelForLevel;
import static dms.pastor.chinesegame.utils.DomUtils.getResultTimeAsString;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;
import static java.lang.String.format;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 15/01/2013
 */
public final class SapperGame extends Level implements View.OnClickListener {
    private final SapperScoreCalculator calculator = new SapperScoreCalculator();
    private SharedPreferences prefs;
    private ArrayList<Word> words;
    private Player player;
    private DomTimer timer;
    private Vibrator vibrator;
    private HighScore highScore;
    private Word answerWord;
    private Word wrongWord1;
    private Word wrongWord2;
    private Word wrongWord3;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button spellCureButton;
    private TextView currentCharacter;
    private TextView currentPinyin;
    private Statistic statistic;
    private TextView score, bonusScore;
    private TextView lvl;
    private TextView playerState;
    private TextView timeElapsed;
    private LinearLayout topLine2;
    private LinearLayout spellRow1;
    private LinearLayout spellRow2;
    private TextView correctValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.level);
        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);

        bonusScore = (TextView) findViewById(R.id.bonus_score);
        correctValue = (TextView) findViewById(R.id.correct_value);
        currentCharacter = (TextView) findViewById(R.id.currentCharacter);
        currentPinyin = (TextView) findViewById(R.id.currentPinyin);
        highScoreTextView = (TextView) findViewById(R.id.high_score);
        highScoreTextView.setVisibility(View.INVISIBLE);
        playerState = (TextView) findViewById(R.id.player_states);
        lvl = (TextView) findViewById(R.id.current_lvl);
        Button removeBadAnswerButton = (Button) findViewById(R.id.spell_remove_wrong_button);
        score = (TextView) findViewById(R.id.current_score);

        timeElapsed = (TextView) findViewById(R.id.time_elasped_value);

        topLine2 = (LinearLayout) findViewById(R.id.top_line2);
        spellRow1 = (LinearLayout) findViewById(R.id.spell_row1);
        spellRow2 = (LinearLayout) findViewById(R.id.spell_row2);
        spellCureButton = (Button) findViewById(R.id.spell_cure_button);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        removeBadAnswerButton.setOnClickListener(this);

        UIUtils.loadAd(this, this);

        setup();

        setupUI();
        setupTurn();
        updatePlayer();

        super.runTimer();

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
    public void endOfLevel() {
        setEnabled(false);
        int totalBonus;
        totalBonus = calcCurrentBonus();

        player.setScore((player.getScore() + totalBonus));
        player.game.addToTotalTime(timer.calcTotalTime());
        player.game.addLevel();
        player.game.addCorrect();

        player.setHealth(player.getHealth() + (HEALTH_BONUS_PER_LEVEL));
        player.setMana(player.getMana() + MANA_BONUS_PER_LEVEL);
        setupNewLevel();
    }

    private int calcCurrentBonus() {
        double finalBonus = calculator.calculate(player.game, timer.calcCurrentTime());

        double time = SAPPER_NO_PENALTY_TIME * 1000 - timer.calcCurrentTime();
        if (time > 6750) {
            finalBonus += 5;
        } else if (time > 500) {
            finalBonus = ((time) / ((SAPPER_NO_PENALTY_TIME * 1000))) * finalBonus;
        } else {
            finalBonus = (getDifficultyLevelForLevel(player.game.getLevel()) + player.game.getLevel()) * (-1);
        }


        if (prefs.getBoolean("sapperPinyin", false)) {
            return (int) finalBonus;
        } else {
            return (int) finalBonus * 2;
        }

    }

    @Override
    public void setupTurn() {

        DomUtils.shuffle(words);
        answer1Button.setText(words.get(0).getWordInEnglish());
        answer2Button.setText(words.get(1).getWordInEnglish());
        answer3Button.setText(words.get(2).getWordInEnglish());
        answer4Button.setText(words.get(3).getWordInEnglish());
        setEnabled(true);
        timer = new DomTimer();
        timer.resetTimer();
        timer.start();

        player.nextTurn();
    }

    @Override
    public void setup() {
        player = Player.getPlayer();
        highScore = HighScore.getHighScore();
        statistic = Statistic.getStatistic(this);
        words = new ArrayList<>();
        do {
            answerWord = player.game.getRandomWordForLevel();
        } while (answerWord == null);
        wrongWord1 = selectAWord(wrongWord1, new Word[]{answerWord});
        wrongWord2 = selectAWord(wrongWord2, new Word[]{answerWord, wrongWord1});
        wrongWord3 = selectAWord(wrongWord3, new Word[]{answerWord, wrongWord1, wrongWord2});
        words.add(answerWord);
        words.add(wrongWord1);
        words.add(wrongWord2);
        words.add(wrongWord3);
        player.game.timeStart();
    }

    @Override
    protected void setEnabled(boolean enabled) {
        answer1Button.setEnabled(true);
        answer2Button.setEnabled(true);
        answer3Button.setEnabled(true);
        answer4Button.setEnabled(true);
    }

    @Override
    public void setupUI() {
        if (prefs.getBoolean("sapperPinyin", false) && answerWord.getDifficulty() < Config.HIDE_PINYIN_ON_EASY_ON_WORD_DIFFICULTY) {
            currentPinyin.setVisibility(VISIBLE);
        } else {
            currentPinyin.setVisibility(TextView.INVISIBLE);
        }
        topLine2.setVisibility(View.GONE);
        spellRow1.setVisibility(View.GONE);
        spellRow2.setVisibility(View.GONE);
        spellCureButton.setVisibility(View.GONE);
        UIUtils.setAllToDefault(this, answer1Button, answer2Button, answer3Button, answer4Button);
        currentCharacter.setText(answerWord.getChineseCharacter());
        currentPinyin.setText(answerWord.getPinyin());
        score.setText(String.valueOf(player.getScore()));
        lvl.setText(String.valueOf(player.game.getLevel()));
    }

    @Override
    public void updatePlayer() {
        updateUI();
    }

    @Override
    protected void updateUI() {
        score.setText(String.valueOf(player.getScore()));
        int currentBonus = calcCurrentBonus();
        bonusScore.setText(format("(%s)", String.valueOf(currentBonus)));
        if (currentBonus > calculator.calculate(player.game, timer.calcCurrentTime()) / 2) {
            setTextColor(bonusScore, R.color.big_bonus, this);
        } else if (currentBonus > calculator.calculate(player.game, timer.calcCurrentTime()) / 5) {
            setTextColor(bonusScore, R.color.small_bonus, this);
        } else if (currentBonus > (-1) * calculator.calculate(player.game, timer.calcCurrentTime()) / 10) {
            setTextColor(bonusScore, R.color.near_zero_bonus, this);
        } else if (timer.calcCurrentTime() <= SAPPER_NO_PENALTY_TIME * SECONDS) {
            setTextColor(bonusScore, R.color.penalty_bonus, this);
        } else {
            bonusScore.setText(R.string.dead);
            setTextColor(bonusScore, R.color.game_over, this);
        }
        int hsPlace = highScore.getCurrentPlaceFor(player.getScore(), player.game.getGameType());
        if (hsPlace > 0) {
            this.highScoreTextView.setText(format(" HS:[%s] ", String.valueOf(hsPlace)));
            this.highScoreTextView.setVisibility(VISIBLE);
            setHSPlaceColor(hsPlace);

        }

        playerState.setText(player.getPlayerState());
        setTextColor(playerState, R.color.status, this);

        timeElapsed.setText(getResultTimeAsString(player.game.getCurrentTime()));
        correctValue.setText(String.valueOf(player.game.getCorrect()));
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
            default:
                Toast.makeText(this, getString(R.string.woops), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer(Button button) {
        timer.stop();
        if (super.isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {

            int healthPenalty = Config.calcPenaltyHealthForTime(timer.calcCurrentTime());

            if (healthPenalty > SAPPER_NO_PENALTY_TIME) {
                dead(getString(R.string.game_over_timeout));
            } else {
                endOfLevel();
            }
        } else {
            if (vibrator != null && prefs.getBoolean("vibrate", Config.DEFAULT_VIBRATE)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (prefs.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND)) {
                playTestTune(this);
            }
            statistic.addWordToWordMistake(getWordIdFromWrong(words, button.getText().toString()));
            dead(getString(R.string.game_over_wrong_answer) + answerWord.getChineseCharacter() + "\n\t" + answerWord.getPinyin() + "\n\t" + answerWord.getWordInEnglish().toUpperCase() + "");
        }
    }

    private void setupNewLevel() {
        setup();
        setupUI();
        setupTurn();
        updatePlayer();
    }

    private void dead(String reason) {
        Toast.makeText(this, getString(R.string.game_over) + reason, Toast.LENGTH_LONG).show();
        Intent ii;
        ii = new Intent(this, SapperResult.class);
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
        finish();
    }


}