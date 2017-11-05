package dms.pastor.chinesegame.learning.lessons;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.learning.lessons.Lesson;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.menu.LearningMenu;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 05/01/2013
 */
public final class LessonSelection extends ListActivity {
    private static final String TAG = "LESSON SELECTION";
    private String[] lessonItems;
    private Player player;
    private List<Lesson> lessons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        player = Player.getPlayer(this);
        setup();
        setListAdapter(new ArrayAdapter<>(this, R.layout.selection_list, lessonItems));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLesson = ((TextView) view).getText().toString();
            setupATest(selectedLesson);
        });
    }

    private void setupATest(String selectedLesson) {
        Log.d(TAG, "setting up level");
        player.initializeLessons(lessons);
        player.setSelectedLesson(selectedLesson);
        Result r = player.miniLessons.findLesson(selectedLesson);
        if (r.isFail()) {
            error("Failed to load lessons due " + r.getMessage());
        } else {
            Log.d(TAG, r.getMessage());
            player.restart(GameType.LESSON);
            player.getGame().setGameWordList(Dictionary.getDictionary().getWordsFromCategoryFromDictionary(player.miniLessons.getCurrentLesson().getGroup()));
            if (player.getGame() != null && player.getGame().getGameWordsList().size() < 4) {
                error("Lesson not ready yet.");
            } else {
                Intent select = new Intent(getApplicationContext(), SingleLesson.class);
                select.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(select);
            }

        }
    }


    private void setup() {
        DatabaseManager dbManager = DatabaseManager.getDbManager(this);
        if (dbManager != null) {
            lessons = DatabaseManager.getDbService().getAllLessons();
            if (lessons != null) {
                lessonItems = dbManager.getLessonsAsWordList(lessons);
            } else {
                error("No lessons found.");
            }
        } else {
            error("There is a problem dbManager. Go to option and select 'Diagnose and fix..' ");
        }
    }

    private void error(String msg) {

        DomUtils.displayError(this, "Unable  to load a lesson.  Restart game.");
        Log.w(TAG, "WOOPS!\n" + msg);
        Intent inMain = new Intent(this, LearningMenu.class);
        inMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inMain);
    }

}