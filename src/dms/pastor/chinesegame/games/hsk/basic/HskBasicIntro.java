package dms.pastor.chinesegame.games.hsk.basic;

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
import dms.pastor.chinesegame.data.game.Intro;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.UIUtils;

/**
 * Author: Pastor
 * WWW: http://pastor.ovh.org
 * Date: 30.11.12 21:17
 */
public final class HskBasicIntro extends Intro implements View.OnClickListener {
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
        introTitle.setText(getResources().getString(R.string.hsk_basics_title));
        TextView introAbout = (TextView) findViewById(R.id.intro_about);
        introAbout.setText(getResources().getString(R.string.hsk_basics_about));
        TextView introTutorial = (TextView) findViewById(R.id.intro_tutorial);
        introTutorial.setText(getResources().getString(R.string.hsk_tutorial));
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("showIntro", true)) {
            start();
        }
        UIUtils.loadAd(this, this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play_game_button:
                start();
        }
    }

    private void start() {

        player.restart(GameType.HSK0);
        player.game.timeStart();
        Intent select;
        select = new Intent(getApplicationContext(), HSKLevel.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(select);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }


}
