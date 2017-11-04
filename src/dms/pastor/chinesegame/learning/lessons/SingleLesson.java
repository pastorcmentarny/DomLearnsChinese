package dms.pastor.chinesegame.learning.lessons;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 02/01/2013
 */
public final class SingleLesson extends Activity implements View.OnClickListener {

    private static final String TAG = "A LESSON";
    private Player player;

    private ToggleButton lessonSwitch;

    private TextView lessonTitle;
    private TextView lessonContent;
    private ListView wordList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lesson);

        lessonTitle = findViewById(R.id.lesson_title);
        lessonContent = findViewById(R.id.lesson_content);
        Button lessonTestButton = findViewById(R.id.lesson_test_button);
        lessonSwitch = findViewById(R.id.lesson_switch);
        lessonTestButton.setOnClickListener(this);
        lessonSwitch.setOnClickListener(this);
        wordList = findViewById(R.id.wordList);

        player = Player.getPlayer();

        setupGUI();
    }

    @Override
    public void onClick(View view) {
        Intent select;
        switch (view.getId()) {
            case R.id.lesson_test_button:
                select = new Intent(getApplicationContext(), LessonTest.class);
                select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(select);
                break;
            case R.id.lesson_switch:
                if (lessonSwitch.isChecked()) {
                    wordList.setVisibility(View.VISIBLE);
                    lessonContent.setVisibility(View.VISIBLE);
                } else {
                    wordList.setVisibility(View.VISIBLE);
                    lessonContent.setVisibility(View.GONE);
                }
                break;
            default:
                Log.w(TAG, "Unimplemented ");
                break;
        }
    }

    //TODO fix it as it doesn't work!
    private void setupGUI() {
        lessonTitle.setText(DomUtils.getUnknownWhenNullString(player.miniLessons.getCurrentLesson().getTitle()));
        lessonContent.setText(DomUtils.getUnknownWhenNullString(player.miniLessons.getCurrentLesson().getLessonContent()));
        ArrayList<String> tmpList = new ArrayList<>();

        for (Word word : player.getGame().getGameWordsList()) {
            Log.w(TAG, word.getWordInEnglish());
            tmpList.add("\n-------------\n=============\n" + word.getChineseCharacter() + "(" + word.getStrokes() + ")\n" + word.getPinyin() + "\n" + word.getWordInEnglish() + "\nNotes: " + word.getNotes());
        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.content_list, tmpList);
        wordList.setAdapter(listAdapter);
        wordList.setTextFilterEnabled(true);
        listAdapter.setNotifyOnChange(true);
        listAdapter.notifyDataSetChanged();
    }

}
