package dms.pastor.chinesegame.learning.sentencespattern;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.learning.patterns.Pattern;
import dms.pastor.chinesegame.db.DatabaseManager;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 05/01/2013
 */
public final class PatternSelection extends ListActivity {
    private DatabaseManager dbManager;
    private String[] wordsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dbManager = DatabaseManager.getDbManager(this);
        if (dbManager != null) {
            List<Pattern> patterns = DatabaseManager.getDbService().getAllPatterns();
            if (patterns != null) {
                wordsList = DatabaseManager.getPatternsAsWordList(patterns);
            } else {
                finish();
            }
        } else {
            finish();
        }
        setListAdapter(new ArrayAdapter<>(this, R.layout.selection_list, wordsList));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String patternTitle = ((TextView) view).getText().toString();
                Pattern pattern = null;
                try {
                    pattern = dbManager.findPatternByTitle(patternTitle);
                } catch (NotFoundException e) {
                    error(e.getMessage());
                }
                Intent ii = new Intent(getApplicationContext(), PatternLesson.class);
                Player player = Player.getPlayer();
                player.setSelectedPattern(pattern);
                startActivity(ii);
            }
        });

    }

    private void error(String errorMsg) {
        Log.w(getString(R.string.pattern_into), "Woops.Don't panic.Just .Try again.If problem still exist.Inform me about it.I will fix it.\n" + errorMsg + "\n");
        finish();
    }

}
