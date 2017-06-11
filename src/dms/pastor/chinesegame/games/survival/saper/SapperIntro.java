package dms.pastor.chinesegame.games.survival.saper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Intro;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.UIUtils;

/**
 * User: Pastor
 * Date: 19.02.13
 * Time: 21:37
 */
public final class SapperIntro extends Intro implements View.OnClickListener {
    private Player player;
    private Statistic statistic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_intro);

        Button startGame = (Button) findViewById(R.id.play_game_button);
        startGame.setOnClickListener(this);
        TextView introTitle = (TextView) findViewById(R.id.intro_title);
        introTitle.setText(getResources().getString(R.string.saper_game_title));
        TextView introAboutGame = (TextView) findViewById(R.id.intro_about);
        introAboutGame.setText(getResources().getString(R.string.saper_game_about));
        TextView introTutorial = (TextView) findViewById(R.id.intro_tutorial);
        introTutorial.setText(getResources().getString(R.string.saper_tutorial));
        player = Player.getPlayer();
        statistic = Statistic.getStatistic(this);
        UIUtils.loadAd(this, this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SapperIntro.this);
        if (!preferences.getBoolean("show_sapper_intro", true)) {
            start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_game_button:
                start();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
        }
    }

    private void start() {
        Intent ii = new Intent(getApplicationContext(), SapperGame.class);
        player.restart(GameType.SAPPER);
        statistic.addSapperGame();
        player.game.setGameWordList(Dictionary.getDictionary().getWordsForLevel(1));
        startActivity(ii);
    }

}
