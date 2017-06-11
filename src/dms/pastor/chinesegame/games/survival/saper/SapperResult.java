package dms.pastor.chinesegame.games.survival.saper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import dms.pastor.chinesegame.games.GameResult;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.utils.DomUtils.getVersionCode;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 31/01/2013
 */
public final class SapperResult extends GameResult {
    private static final String TAG = "SAPPER RESULT";
    private Player player;
    private Statistic statistic;
    private HighScore hs;
    private TextView gameOverTitle;
    private TextView resultGrade;
    private TextView resultScore;
    private TextView resultTime;
    private TextView correctAnswersValue;
    private TextView questionsValue;
    private TextView recordText;
    private Score sScore;
    private SharedPreferences options;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.result_score);
        hs = HighScore.getHighScore();
        options = getSharedPreferences("settings", Context.MODE_PRIVATE);
        statistic = Statistic.getStatistic(this);

        gameOverTitle = (TextView) findViewById(R.id.result);
        resultGrade = (TextView) findViewById(R.id.result_grade);
        resultScore = (TextView) findViewById(R.id.result_score);
        resultTime = (TextView) findViewById(R.id.result_time);
        correctAnswersValue = (TextView) findViewById(R.id.correct_answers_value);
        questionsValue = (TextView) findViewById(R.id.questions_value);
        recordText = (TextView) findViewById(R.id.recordtext);
        placeText = (TextView) findViewById(R.id.result_place_text);


        LinearLayout mistakeRow = (LinearLayout) findViewById(R.id.mistake_row);
        mistakeRow.setVisibility(View.GONE);
        LinearLayout questionsRow = (LinearLayout) findViewById(R.id.question_row);
        questionsRow.setVisibility(View.GONE);

        Button backToMainMenu = (Button) findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);

        Button tryAgain = (Button) findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);

        Button seeHS = (Button) findViewById(R.id.seeHS);
        seeHS.setOnClickListener(this);
        seeHS.setVisibility(View.VISIBLE);
        UIUtils.loadAd(this, this);

        setup();
    }

    private void setup() {
        player = Player.getPlayer();
        HighScore highScore = HighScore.getHighScore();
        String diff;
        if (options.getBoolean("sapperPinyin", false)) {
            diff = Difficulty.EASY.name();
        } else {
            diff = Difficulty.HARD.name();
        }
        sScore = new Score(player.getName(this), player.getScore(),
                player.game.getLevel(), new SimpleDateFormat(Config.DATE_FORMAT, Locale.ENGLISH).format(new Date()),
                statistic.getGames(), getVersionCode(this),
                new Date().getTime(), diff);
        try {
            if (highScore != null) {
                highScore.addToHighScore(sScore, player.game.getGameType());
            }
        } catch (Exception e) {
            Log.w(TAG, getResources().getString(R.string.highscore_restarted_due_error));
            Toast.makeText(this, getResources().getString(R.string.highscore_restarted_due_error), Toast.LENGTH_LONG).show();
            highScore.fixHighScores();
        }
        gameOverTitle.setText(getString(R.string.game_over));
        resultGrade.setVisibility(View.GONE);
        resultScore.setText(String.valueOf(player.getScore() > 0 ? player.getScore() : 0));
        resultTime.setText(DomUtils.getResultTimeAsString(player.game.getTotalTime()));
        correctAnswersValue.setText(String.valueOf(player.game.getCorrect()));
        questionsValue.setText(String.valueOf(player.game.getLevels()));
        statistic.addTotalLevels(player.game.getLevel());
        statistic.addToHighestLevelSapper(player.game.getLevel());
        statistic.addCorrects(player.game.getCorrect());
        statistic.addWrong();
        statistic.addToMaxScore(player.getScore());
        statistic.addToTotalTimeSapper(player.game.getTotalTime());
        statistic.save();
        checkHighScore();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToMainMenu:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.tryAgain:
                Intent ii = new Intent(getApplicationContext(), SapperGame.class);
                player.restart(GameType.SAPPER);
                player.game.setGameWordList(Dictionary.getDictionary().getWordsForLevel(1));
                statistic.addSapperGame();
                startActivity(ii);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.seeHS:
                HighScore highScore = HighScore.getHighScore();
                if (highScore != null) {
                    Toast.makeText(getApplicationContext(), highScore.displayTop10For(GameType.SAPPER), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.highscore_no_available), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DomUtils.goToHome(this, SapperResult.this);
        super.onBackPressed();
    }

    private void checkHighScore() {
        int place = hs.getCurrentPlaceFor(sScore.getScore(), player.game.getGameType());
        if (place == 1) {
            resultGrade.setText(R.string.new_high_score);
            UIUtils.newRecord(this, placeText, this);
            placeText.setText(String.valueOf(place));
            setHSPlaceColor(place);
        } else {
            if (place > 1) {
                placeText.setText(String.valueOf(place));
                setHSPlaceColor(place);
            }

        }

        if (hs.getScoresFor(GameType.SAPPER).size() > 0) {
            recordText.setText(String.format("RECORD: %s - %s", hs.getScoresFor(GameType.SAPPER).get(0).getPlayerName(), hs.getScoresFor(GameType.SAPPER).get(0).asHighScore()));
        }

    }
}
