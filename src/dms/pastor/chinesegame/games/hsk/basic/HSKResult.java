package dms.pastor.chinesegame.games.hsk.basic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.appbrain.AppBrain;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.common.enums.Grades;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;

import static dms.pastor.chinesegame.Config.calculateScore;
import static dms.pastor.chinesegame.data.game.Player.getPlayer;

/**
 * .
 * Author: Pastor
 * WWW: http://pastor.ovh.org
 * Date: 26.11.12 19:06
 */
public final class HSKResult extends Activity implements View.OnClickListener {
    private static final String TAG = "HSK RESULT";
    private Player player;
    private TextView resultGrade;
    private TextView resultScore;
    private TextView resultTime;
    private TextView correctAnswersValue;
    private TextView mistakesValue;
    private TextView questionsValue;


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
        questionsValue = (TextView) findViewById(R.id.questions_value);
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
        player = getPlayer();
        int score = calculateScore();

        resultGrade.setText(Grades.giveAGrade(this, score));
        score = DomUtils.getResultIn0to100Range(score);
        resultScore.setText(String.format("%s %%", String.valueOf(score)));
        resultTime.setText(DomUtils.getResultTimeAsString(player.game.getTotalTime()));
        correctAnswersValue.setText(String.valueOf(player.game.getCorrect()));
        mistakesValue.setText(String.valueOf(player.game.getMistake()));
        questionsValue.setText(String.valueOf(player.game.getLevels()));

    }

    private void restart() {
        player.restart(GameType.HSK0);
        Dictionary dictionary = Dictionary.recreateDictionary();
        dictionary.readDictionaryFromFile(this, R.raw.dictionary, new String[]{"hsk1"});
        player.game.setAnswerWordsForLevels(dictionary.getWordsList());
        player.game.setGameWordList(dictionary.getWordsList());
        player.game.createAnswerWordsForLevels(player.game.getAnswerWordsForLevels());
        player.game.timeStart();
        Intent select;
        select = new Intent(getApplicationContext(), HSKLevel.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(select);
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        //finish();
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
