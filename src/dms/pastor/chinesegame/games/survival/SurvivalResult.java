package dms.pastor.chinesegame.games.survival;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.data.game.score.Score;
import dms.pastor.chinesegame.games.GameResult;
import dms.pastor.chinesegame.games.survival.word.WordSurvival;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.utils.DomUtils.getVersionCode;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * Email: email can be found on my website
 * <p>
 * Created 31.01.2013
 */
public final class SurvivalResult extends GameResult {

    private static final String TAG = "Survival RESULT";
    private HighScore highScore;
    private Player player;
    private Score sScore;
    private Statistic statistic;
    private InterstitialAd interstitial = null;
    private Button backToMainMenu, tryAgain, seeHS;
    private final AdListener adl = new AdListener() {
        @Override
        public void onAdLoaded() {
            displayInterstitial();
            setVisibilityForMenuItems(View.VISIBLE);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            setVisibilityForMenuItems(View.VISIBLE);
        }
    };
    private CountDownTimer countDownTimer;
    private TextView questionsValue, gameOverTitle, resultGrade;
    private TextView resultScore, resultTime, correctAnswersValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.result_score);
        player = Player.getPlayer();
        if (player == null || player.game == null) {
            Log.i(TAG, "No game data available");
            finish();
        }
        statistic = Statistic.getStatistic(this);
        statistic.addToTotalTimeAdventure(player.game.getTotalTime());
        statistic.addToHighestLevelAdventures(player.game.getLevel());
        statistic.save();
        try {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(Config.AD_INTERSTITIAL_UNIT);
            interstitial.setAdListener(adl);
            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
        } catch (Exception e) {
            Log.w(TAG, "Problem with interstitial");
        }


        gameOverTitle = (TextView) findViewById(R.id.result);
        resultGrade = (TextView) findViewById(R.id.result_grade);
        resultScore = (TextView) findViewById(R.id.result_score);
        resultTime = (TextView) findViewById(R.id.result_time);
        correctAnswersValue = (TextView) findViewById(R.id.correct_answers_value);
        questionsValue = (TextView) findViewById(R.id.questions_value);
        placeText = (TextView) findViewById(R.id.result_place_text);

        LinearLayout mistakeRow = (LinearLayout) findViewById(R.id.mistake_row);
        mistakeRow.setVisibility(View.GONE);
        LinearLayout questionsRow = (LinearLayout) findViewById(R.id.question_row);
        questionsRow.setVisibility(View.GONE);

        backToMainMenu = (Button) findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);

        tryAgain = (Button) findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);

        highScore = HighScore.getHighScore();
        seeHS = (Button) findViewById(R.id.seeHS);
        seeHS.setOnClickListener(this);
        setVisibilityForMenuItems(View.GONE);

        setup();

    }

    @Override
    protected final void onResume() {
        super.onResume();
        activateCountTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        statistic.save();

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToMainMenu:
                DomUtils.goToHome(this, SurvivalResult.this);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.tryAgain:
                Intent ii = new Intent(getApplicationContext(), WordSurvival.class);
                GameType tmp = player.game.getGameType();
                player.restart(tmp);
                player.game.setGameWordList(Dictionary.getDictionary().getWordsForLevel(1));
                player.game.timeStart();
                statistic.addAdventureGame();
                startActivity(ii);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case R.id.seeHS:
                if (highScore != null) {
                    Toast.makeText(getApplicationContext(), highScore.displayTop10For(player.game.getGameType()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.highscore_no_available), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void setup() {

        HighScore highScore = HighScore.getHighScore();
        sScore = new Score(player.getName(this), player.getScore(),
                player.game.getLevel(), new SimpleDateFormat(Config.DATE_FORMAT, Locale.ENGLISH).format(new Date()),
                statistic.getGames(), getVersionCode(this),
                new Date().getTime());
        try {
            if (highScore != null) {

                highScore.addToHighScore(sScore, player.game.getGameType());
            }
        } catch (Exception e) {
            Log.w(TAG, getResources().getString(R.string.highscore_restarted_due_error) + e.getMessage());
            UIUtils.displayError(this, getResources().getString(R.string.highscore_restarted_due_error) + e.getMessage());
        }
        gameOverTitle.setText(getString(R.string.game_over));
        resultGrade.setVisibility(View.GONE);
        resultScore.setText(String.valueOf(player.getScore() > 0 ? player.getScore() : 0));
        resultTime.setText(DomUtils.getResultTimeAsString(player.game.getTotalTime()));
        correctAnswersValue.setText(String.valueOf(player.game.getCorrect()));
        questionsValue.setText(String.valueOf(player.game.getLevels()));
        checkHighScore();
    }

    private void checkHighScore() {
        int place = highScore.getCurrentPlaceFor(sScore.getScore(), player.game.getGameType());
        if (place > 0) {
            placeText.setText(String.valueOf(place));
            setHSPlaceColor(place);
        }
    }

    private void activateCountTimer() {
        countDownTimer = new CountDownTimer(1000, 2000) {

            public void onTick(long millisUntilFinished) {
                //NOT IN USE :)
            }

            public void onFinish() {
                backToMainMenu.setVisibility(View.VISIBLE);
                tryAgain.setVisibility(View.VISIBLE);
                seeHS.setVisibility(View.VISIBLE);

            }
        };

        countDownTimer.start();
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void setVisibilityForMenuItems(int state) {
        backToMainMenu.setVisibility(state);
        tryAgain.setVisibility(state);
        seeHS.setVisibility(state);
    }
}