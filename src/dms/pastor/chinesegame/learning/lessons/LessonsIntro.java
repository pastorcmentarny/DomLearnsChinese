package dms.pastor.chinesegame.learning.lessons;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dms.pastor.chinesegame.R;

/**
 * User: Pastor
 * Date: 02.01.13
 * Time: 21:46
 */
public final class LessonsIntro extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lesson_intro);

        Button lessonSelectionButton = (Button) findViewById(R.id.lesson_selection_button);
        lessonSelectionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent select;
        switch (view.getId()) {
            case R.id.lesson_selection_button:
                select = new Intent(getApplicationContext(), LessonSelection.class);
                select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(select);
        }
    }

}
