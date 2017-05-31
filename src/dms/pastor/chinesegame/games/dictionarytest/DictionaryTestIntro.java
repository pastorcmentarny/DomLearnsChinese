package dms.pastor.chinesegame.games.dictionarytest;

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
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.game.Intro;
import dms.pastor.chinesegame.data.game.Player;

/**
 * Author: Pastor
 * WWW: http://pastor.ovh.org
 * Date: 30.11.12 21:17
 */
public final class DictionaryTestIntro extends Intro implements View.OnClickListener {
    private final Player player = Player.getPlayer();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_intro);
        Button startGame = (Button) findViewById(R.id.play_game_button);
        startGame.setOnClickListener(this);
        TextView introTitle = (TextView) findViewById(R.id.intro_title);
        introTitle.setText(getResources().getString(R.string.dictionary_test_title));
        TextView introAbout = (TextView) findViewById(R.id.intro_about);
        introAbout.setText(getResources().getString(R.string.dictionary_test_about));
        TextView introTutorial = (TextView) findViewById(R.id.intro_tutorial);
        introTutorial.setText(getResources().getString(R.string.dictionary_test_tutorial));
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("showIntro", true)) {
            start();
        }
        setAd();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play_game_button:
                start();
        }
    }

    private void start() {
        player.restart(GameType.DICTIONARY_TEST);
        Statistic.getStatistic(this).addDictionaryTestGame();
        player.game.timeStart();
        Intent select;
        select = new Intent(getApplicationContext(), DictionaryTestGame.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(select);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }


}
