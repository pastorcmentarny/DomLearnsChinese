package dms.pastor.chinesegame.learning.chats;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.ads.AdView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.Utils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 02/01/2013
 */
public final class ChatIntro extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.chat_intro);
        Button chatSelectionButton = (Button) findViewById(R.id.lesson_selection_button);
        chatSelectionButton.setOnClickListener(this);
        displayAd();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lesson_selection_button:
                goToChatSelectionActivity();
        }
    }

    private void goToChatSelectionActivity() {
        Intent select;
        select = new Intent(getApplicationContext(), ChatSelection.class);
        select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(select);
    }

    private void displayAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            com.google.android.gms.ads.AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }

}
