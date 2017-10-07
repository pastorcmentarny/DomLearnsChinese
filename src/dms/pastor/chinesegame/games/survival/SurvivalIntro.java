package dms.pastor.chinesegame.games.survival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Intro;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.games.survival.word.WordSurvival;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.common.enums.GameType.ADVENTURE;
import static dms.pastor.chinesegame.data.game.Player.getPlayer;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 12/12/2012
 */
public final class SurvivalIntro extends Intro {

    private final Player player = getPlayer();
    private TextView introTitle, introAbout, introTutorial;
    private Statistic statistic;
    private Bundle extras;
    private String gameType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_intro);
        statistic = Statistic.getStatistic(this);
        extras = getIntent().getExtras();
        gameType = extras.getString("GAME");

        Button startGame = findViewById(R.id.play_game_button);
        startGame.setOnClickListener(this);
        introTitle = findViewById(R.id.intro_title);
        introAbout = findViewById(R.id.intro_about);
        introTutorial = findViewById(R.id.intro_tutorial);
        UIUtils.loadAd(this, this);

        updateUI();
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("showIntro", true)) {
            start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_game_button:
                start();
                break;
        }
    }

    private void start() {
        Intent ii;
        String selectedGameType = extras.getString("GAME");

        if (selectedGameType != null) {
            if (selectedGameType.equalsIgnoreCase(ADVENTURE.name().toLowerCase())) {
                ii = new Intent(getApplicationContext(), WordSurvival.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                player.restart(ADVENTURE);
                statistic.addAdventureGame();
                player.getGame().setGameWordList(Dictionary.getDictionary().getWordsForLevel(1));
                player.getGame().timeStart();
                startActivity(ii);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }

        }
    }

    private void updateUI() {


        if (gameType != null) {

            if (gameType.equals("ADVENTURE")) {
                introTitle.setText(getResources().getString(R.string.adventure_game));
                introAbout.setText(getResources().getString(R.string.adventure_about));
                introTutorial.setText(getResources().getString(R.string.adventure_tutorial));
            }
        }
    }

}
