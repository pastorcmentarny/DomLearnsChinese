package dms.pastor.chinesegame.learning.lessons;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.learning.lessons.Lesson;
import dms.pastor.chinesegame.utils.Result;

import static dms.pastor.chinesegame.utils.DomUtils.goToHome;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 02/01/2013
 */
public final class LessonResult extends Activity implements View.OnClickListener {
    private static final String TAG = "LESSON TEST RESULT";
    private final Player player = Player.getPlayer();
    private TextView resultTextView;
    private Button backToMenu;
    private Button goToNextLevel;
    private Button resetLevel;
    private InterstitialAd interstitial = null;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupScreen();
        setupAd();

        setupScreenViews();
        setupListenerForButtons();
        setVisibilityForMenuItems(View.GONE);

        setupGUI();
    }

    private void setupScreenViews() {
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        backToMenu = (Button) findViewById(R.id.backToMainMenu);
        goToNextLevel = (Button) findViewById(R.id.goToNextLevel);
        resetLevel = (Button) findViewById(R.id.restart_level);
    }

    private void setupListenerForButtons() {
        resetLevel.setOnClickListener(this);
        backToMenu.setOnClickListener(this);
        goToNextLevel.setOnClickListener(this);
    }

    private void setupScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.campaign_result);
    }

    private void setupAd() {
        try {
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(Config.AD_INTERSTITIAL_UNIT);
            interstitial.setAdListener(adl);
            com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
        } catch (Exception e) {
            Log.w(TAG, "Problem with interstitial");
        }
    }

    private void setupGUI() {
        resultTextView.setText(getResult());
    }

    @Override
    public void onBackPressed() {
        goToHome(this, LessonResult.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goToNextLevel:
                goToNewLevel();
                break;
            case R.id.restart_level:
                resetLevel();
                break;
            case R.id.backToMainMenu:
                goToHome(this, LessonResult.this);
                break;
            default:
                Log.w(TAG, "woops!");
                break;
        }
    }

    private void resetLevel() {
        Log.d(TAG, "resetting level");

        player.restart(GameType.LESSON);

        Result r = player.miniLessons.findLesson(player.getSelectedLesson());
        if (r.isFail()) {
            Log.w(TAG, r.getMessage());
            finish();
        } else {
            Log.i(TAG, r.getMessage());

            setupDictionaryForNewLevel();

            Intent select;
            select = new Intent(getApplicationContext(), SingleLesson.class);
            player.setSelectedLesson(player.miniLessons.getCurrentLesson().getTitle());
            select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(select);
        }
    }


    private void goToNewLevel() {
        Log.d(TAG, "going to next level");
        try {
            player.restart(GameType.LESSON);
            setupNextLesson();
            setPlayerForNewLevel();
            startActivity(getIntentForSingleLesson());
        } catch (Exception e) {
            Log.i(TAG, "No more lessons available.");
            goToNextLevel.setVisibility(View.GONE);
        }
    }

    private void setupNextLesson() {
        int nextLessonId = player.miniLessons.getCurrentLesson().getId() + 1;

        for (Lesson l : player.miniLessons.getLessons()) {
            if (l.getId() == nextLessonId) {
                player.miniLessons.setCurrentLesson(l);
                player.setSelectedLesson(l.getTitle());
                break;
            }
        }
    }

    private void setPlayerForNewLevel() {
        Dictionary dict = setupDictionaryForNewLevel();

        player.game.setLevels(Config.LESSON_LEVELS_SIZE);
        player.game.setAnswerWordsForLevels(dict.getWordsList());
        player.game.setGameWordList(dict.getWordsList());
        player.game.createAnswerWordsForLevels(player.game.getAnswerWordsForLevels());
        player.setSelectedLesson(player.miniLessons.getCurrentLesson().getTitle());
    }

    private Dictionary setupDictionaryForNewLevel() {
        Dictionary dictionary = Dictionary.getDictionary();
        dictionary.readDictionaryFromFile(this, R.raw.dictionary, player.miniLessons.getCurrentLesson().getGroup());
        return dictionary;
    }

    private String getResult() {
        int good = player.game.getCorrect();
        int bad = player.game.getMistake();
        if (bad > 0) {
            bad -= 1;
        }
        int r = good - bad;
        if (r > 6) {
            if (player.miniLessons.getCurrentLesson().getId() < player.miniLessons.getLessons().size()) {
                goToNextLevel.setEnabled(true);
            } else {
                goToNextLevel.setVisibility(View.GONE);
            }
            return "PASS";
        } else {
            goToNextLevel.setEnabled(false);
            return "FAIL";
        }
    }

    // Invoke displayInterstitial() when you are ready to display an interstitial.
    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void setVisibilityForMenuItems(int state) {
        resetLevel.setVisibility(state);
        backToMenu.setVisibility(state);
        goToNextLevel.setVisibility(state);
    }

    private Intent getIntentForSingleLesson() {
        Intent select = new Intent(getApplicationContext(), SingleLesson.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return select;
    }
}