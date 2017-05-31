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

import com.appbrain.AppBrain;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;

import static dms.pastor.chinesegame.utils.DomUtils.displayToast;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

/**
 * .
 * Author: Pastor
 * WWW: http://pastor.ovh.org
 * Date: 26.11.12 19:06
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

        resultGrade = (TextView) findViewById(R.id.result_grade);
        resultScore = (TextView) findViewById(R.id.result_score);
        resultTime = (TextView) findViewById(R.id.result_time);
        correctAnswersValue = (TextView) findViewById(R.id.correct_answers_value);
        mistakesValue = (TextView) findViewById(R.id.mistakes_value);
        skippedValue = (TextView) findViewById(R.id.questions_value);
        recordText = (TextView) findViewById(R.id.recordtext);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(DictionaryTestResult.this);
        TextView questions_title = (TextView) findViewById(R.id.questions_title);
        questions_title.setText(format("%s:", getString(R.string.skipped).toUpperCase()));
        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);
        Button tryAgain = (Button) findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);


        try {
            AppBrain.init(this);
        } catch (Exception e) {
            Log.d(getString(R.string.ab_e), getString(R.string.ab_e_init) + e.getMessage());
        }
        setup();
    }

    private void setup() {
        player = Player.getPlayer();
        int score = player.getScore();
        if (player.game.getTotalTimeInSeconds() > Config.DICTIONARY_TEST_TIME_LIMIT) {
            int penalty = player.game.getTotalTimeInSeconds() - Config.DICTIONARY_TEST_TIME_LIMIT;
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
        resultTime.setText(DomUtils.getResultTimeAsString(player.game.getTotalTime()));
        correctAnswersValue.setText(format(ENGLISH, "%d (%d%%)", player.game.getCorrect(), player.game.getCorrect() / Config.DICTIONARY_TEST_LEVELS_SIZE));
        mistakesValue.setText(String.valueOf(player.game.getMistake()));
        skippedValue.setText(String.valueOf(player.game.getSkipped()));

    }

    private void restart() {
        player.restart(GameType.DICTIONARY_TEST);
        player.game.timeStart();
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
