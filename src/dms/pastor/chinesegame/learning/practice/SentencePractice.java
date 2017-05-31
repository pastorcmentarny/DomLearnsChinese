package dms.pastor.chinesegame.learning.practice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dms.pastor.chinesegame.R;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.Window.FEATURE_NO_TITLE;
import static dms.pastor.chinesegame.utils.Utils.getAdRequest;


public class SentencePractice extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.practice_sentence);

        loadAd();
    }

    private void loadAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            AdRequest adRequest = getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }
}
