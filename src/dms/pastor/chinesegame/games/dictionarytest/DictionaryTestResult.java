package dms.pastor.chinesegame.games.dictionarytest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.game.score.Difficulty;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.data.game.score.Score;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.utils.DomUtils.displayToast;
import static dms.pastor.chinesegame.utils.DomUtils.getVersionCode;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 26/11/2012
 */
public final class DictionaryTestResult extends Activity implements View.OnClickListener {
    private static final String TAG = "HSK RESULT";
    private Player player;
    private SharedPreferences defaultSharedPreferences;
    private TextView resultGrade;
    private TextView resultScore;
    private TextView resultTime;
    private TextView recordText;
    private TextView correctAnswersValue;
    private TextView mistakesValue;
    private TextView skippedValue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.result_grade);

        resultGrade = findViewById(R.id.result_grade);
        resultScore = findViewById(R.id.result_score);
        resultTime = findViewById(R.id.result_time);
        correctAnswersValue = findViewById(R.id.correct_answers_value);
        mistakesValue = findViewById(R.id.mistakes_value);
        skippedValue = findViewById(R.id.questions_value);
        recordText = findViewById(R.id.record_text);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(DictionaryTestResult.this);
        TextView questions_title = findViewById(R.id.questions_title);
        questions_title.setText(format("%s:", getString(R.string.skipped).toUpperCase()));
        Button backToMainMenu = findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);
        Button tryAgain = findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);
        UIUtils.loadAd(this, this);

        setup();
    }

    private void setup() {
        player = Player.getPlayer();
        final Statistic statistic = Statistic.getStatistic(this);
        int score = player.getScore();

        HighScore highScoreBoard = HighScore.getHighScore();
        final Score sScore = new Score(Player.getName(this), player.getScore(),
                player.getGame().getLevel(), new SimpleDateFormat(Config.DATE_FORMAT, Locale.ENGLISH).format(new Date()),
                statistic.getGames(), getVersionCode(this),
                new Date().getTime(), Difficulty.DICTIONARY_TEST.name());
        try {
            if (highScoreBoard != null) {

                highScoreBoard.addToHighScore(sScore, player.getGame().getGameType());
            }
        } catch (Exception e) {
            Log.w(TAG, getResources().getString(R.string.highscore_restarted_due_error) + e.getMessage());
            UIUtils.displayError(this, getResources().getString(R.string.highscore_restarted_due_error) + e.getMessage());
        }



        if (player.getGame().getTotalTimeInSeconds() > Config.DICTIONARY_TEST_TIME_LIMIT) {
            int penalty = player.getGame().getTotalTimeInSeconds() - Config.DICTIONARY_TEST_TIME_LIMIT;
            score -= penalty;
            displayToast(this, format(ENGLISH, "Penalty for slow answers: -%d", penalty));
        }
        int grade = score;
        final int dictionarySize = Dictionary.getDictionary().getAllDictionarySize();
        final int dictionarySizeBonus = Double.valueOf(dictionarySize / 75).intValue();
        score += dictionarySize;
        displayToast(this, format(ENGLISH, "Current dictionary size is: %d. Bonus points + %d", dictionarySize, dictionarySizeBonus));
        final String highScoreDictionaryTest = "highScoreDictionaryTest";
        final int currentHighestScore = defaultSharedPreferences.getInt(highScoreDictionaryTest, 0);
        if (score > currentHighestScore) {
            final SharedPreferences.Editor editor = defaultSharedPreferences.edit();
            editor.putInt(highScoreDictionaryTest, score);
            editor.apply();
            recordText.setText(format(ENGLISH, "NEW RECORD: %d", score));
            setTextColor(recordText, R.color.option_title, this);
        } else {
            recordText.setText(format(ENGLISH, "CURRENT RECORD: %d", score));
            setTextColor(recordText, R.color.option_description, this);
        }


        resultGrade.setText(DictionaryLevelInfo.getAGrade(grade));
        resultScore.setText(format(ENGLISH, "Score: %d ", player.getScore()));
        resultTime.setText(DomUtils.getResultTimeAsString(player.getGame().getTotalTime()));
        correctAnswersValue.setText(format(ENGLISH, "%d (%d%%)", player.getGame().getCorrect(), player.getGame().getCorrect() / Config.DICTIONARY_TEST_LEVELS_SIZE));
        mistakesValue.setText(String.valueOf(player.getGame().getMistake()));
        skippedValue.setText(String.valueOf(player.getGame().getSkipped()));

    }

    private void restart() {
        player.restart(GameType.DICTIONARY_TEST);
        player.getGame().timeStart();
        Statistic.getStatistic(this).addDictionaryTestGame();
        Intent select;
        select = new Intent(getApplicationContext(), DictionaryTestGame.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(select);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToMainMenu:
                finish();
                break;
            case R.id.tryAgain:
                restart();
                break;
            default:
                Log.w(TAG, "woops!");
                break;
        }
    }
}
