package dms.pastor.chinesegame.learning.chats;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.LessonUIMode;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 02/01/2013
 */
public final class ChatLesson extends Activity implements View.OnClickListener {
    private static final String TAG = Config.TAG_PREFIX + "chat lesson";
    private final Player player = Player.getPlayer();

    private TextView lessonTitle, lessonContent;
    private ToggleButton sentenceSwitch, wordSwitch;
    private ListView sentenceListView, wordListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "creating..");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.chat_chapter);

        setupViewForActivity();
        setupGUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sentence_switch:
                if (sentenceSwitch.isChecked()) {
                    changeUITo(LessonUIMode.SENTENCE_LIST);
                } else {
                    changeUITo(LessonUIMode.INFORMATION);
                }
                break;
            case R.id.word_switch:
                if (wordSwitch.isChecked()) {
                    changeUITo(LessonUIMode.WORD_LIST);
                } else {
                    changeUITo(LessonUIMode.INFORMATION);
                }
                break;
            default:
                sentenceSwitch.setChecked(false);
                wordSwitch.setChecked(false);
                changeUITo(LessonUIMode.INFORMATION);
                break;

        }
    }

    private void setupGUI() {
        changeUITo(LessonUIMode.INFORMATION);
        updateGUI();
    }

    private void updateGUI() {
        Log.i(TAG, "update GUI");
        lessonTitle.setText(DomUtils.getUnknownWhenNullString(player.getCurrentChat().getTitle()));
        lessonContent.setText(DomUtils.getUnknownWhenNullString(player.getCurrentChat().getInformation()));

        ArrayAdapter<String> sentenceAA = new ArrayAdapter<>(this, R.layout.content_list, player.getCurrentChat().getSentencesList());
        sentenceListView.setAdapter(sentenceAA);
        sentenceListView.setTextFilterEnabled(true);
        sentenceAA.setNotifyOnChange(true);

        String[] wordList = player.getCurrentChat().getWordsAsArray();
        ArrayAdapter<String> wordAA = new ArrayAdapter<>(this, R.layout.content_list, wordList);
        wordListView.setAdapter(wordAA);
        wordListView.setTextFilterEnabled(true);
        wordAA.setNotifyOnChange(true);
    }

    private void changeUITo(LessonUIMode uiMode) {
        switch (uiMode) {
            case INFORMATION:
                lessonContent.setVisibility(View.VISIBLE);
                sentenceListView.setVisibility(View.GONE);
                wordListView.setVisibility(View.GONE);
                sentenceSwitch.setChecked(false);
                wordSwitch.setChecked(false);
                break;
            case SENTENCE_LIST:
                lessonContent.setVisibility(View.GONE);
                sentenceListView.setVisibility(View.VISIBLE);
                wordListView.setVisibility(View.GONE);
                sentenceSwitch.setChecked(true);
                wordSwitch.setChecked(false);
                break;
            case WORD_LIST:
                lessonContent.setVisibility(View.GONE);
                sentenceListView.setVisibility(View.GONE);
                wordListView.setVisibility(View.VISIBLE);
                sentenceSwitch.setChecked(false);
                wordSwitch.setChecked(true);
                break;
            default:
                Log.w(TAG, "unknown mode selected!");
                lessonContent.setVisibility(View.VISIBLE);
                sentenceSwitch.setChecked(false);
                wordSwitch.setChecked(false);
                sentenceListView.setVisibility(View.GONE);
                wordListView.setVisibility(View.GONE);
                break;
        }
        updateGUI();

    }

    private void setupViewForActivity() {
        lessonTitle = (TextView) findViewById(R.id.chat_title);
        lessonContent = (TextView) findViewById(R.id.chat_content);

        sentenceListView = (ListView) findViewById(R.id.sentencesListView);
        wordListView = (ListView) findViewById(R.id.wordsListView);

        sentenceSwitch = (ToggleButton) findViewById(R.id.sentence_switch);
        sentenceSwitch.setOnClickListener(this);

        wordSwitch = (ToggleButton) findViewById(R.id.word_switch);
        wordSwitch.setOnClickListener(this);
    }
}
