package dms.pastor.chinesegame.learning.practice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.UIUtils;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 02/01/2013
 */
public final class SentencePracticeIntro extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pattern_intro);

        Button lessonSelectionButton = (Button) findViewById(R.id.lesson_selection_button);
        lessonSelectionButton.setOnClickListener(this);
        UIUtils.loadAd(this, this);
    }

    @Override
    public void onClick(View view) {
        Intent select;
        switch (view.getId()) {
            case R.id.lesson_selection_button:
                select = new Intent(getApplicationContext(), SentencePractice.class);
                select.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(select);
        }
    }

}
