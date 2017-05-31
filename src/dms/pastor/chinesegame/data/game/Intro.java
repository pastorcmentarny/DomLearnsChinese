package dms.pastor.chinesegame.data.game;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.Utils;

/**
 * Date: 30/11/12
 * Time: 10:00
 * Intro activity that
 */
public abstract class Intro extends Activity implements View.OnClickListener {

    protected final void setAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            com.google.android.gms.ads.AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }

}
