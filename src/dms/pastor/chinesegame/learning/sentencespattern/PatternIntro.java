package dms.pastor.chinesegame.learning.sentencespattern;

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
 * User: Pastor
 * Date: 02.01.13
 * Time: 21:46
 */
public final class PatternIntro extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pattern_intro);
        Button lessonSelectionButton = (Button) findViewById(R.id.lesson_selection_button);
        lessonSelectionButton.setOnClickListener(this);
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            //set ads

            com.google.android.gms.ads.AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        Intent select;
        switch (view.getId()) {
            case R.id.lesson_selection_button:
                select = new Intent(getApplicationContext(), PatternSelection.class);
                select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(select);
        }
    }

}
