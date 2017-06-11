package dms.pastor.chinesegame.games;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.UIUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 */
public abstract class GameResult extends Activity implements View.OnClickListener {
    protected TextView placeText;

    protected final void setHSPlaceColor(int place) {
        if (place > 75) {
            UIUtils.setTextColor(placeText, R.color.hs75, this);
        } else if (place >= 50) {
            UIUtils.setTextColor(placeText, R.color.hs50, this);
        } else if (place >= 40) {
            UIUtils.setTextColor(placeText, R.color.hs40, this);
        } else if (place >= 30) {
            UIUtils.setTextColor(placeText, R.color.hs30, this);
        } else if (place >= 20) {
            UIUtils.setTextColor(placeText, R.color.hs20, this);
        } else if (place >= 10) {
            UIUtils.setTextColor(placeText, R.color.hs10, this);
        } else if (place > 3) {
            UIUtils.setTextColor(placeText, R.color.hs5, this);
        } else if (place == 3) {
            UIUtils.setTextColor(placeText, R.color.hs3, this);
        } else if (place == 2) {
            UIUtils.setTextColor(placeText, R.color.hs2, this);
        } else if (place == 1) {
            UIUtils.setTextColor(placeText, R.color.hs1, this);
        } else {
            UIUtils.setTextColor(placeText, Color.BLACK, this);
        }
    }
}
