package dms.pastor.chinesegame.learning.dictionary;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashSet;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 18/09/2013
 */
public final class WordFinder extends Activity implements View.OnClickListener {
    private static final String TAG = "Word Finder";
    private Dictionary dictionary;
    private EditText searchField;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dbManager = DatabaseManager.getDbManager(this);
        dictionary = dbManager.getDictionary();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.word_finder);

        searchField = findViewById(R.id.searchField);
        listView = findViewById(R.id.resultView);
        Button findButton = findViewById(R.id.findButton);
        findButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] values = new String[]{"Please type word and press 'Find Word'"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.findButton:
                findAllWords();
                break;
        }
    }

    private void findAllWords() {
        String searchQuery = searchField.getText().toString();

        Log.d(TAG, "search for " + DomUtils.getUnknownWhenNullString(searchQuery));
        HashSet<Word> resultWord = dictionary.findAllWords(searchQuery);


        Log.d(TAG, "Dictionary size is:" + resultWord.size());
        if (!resultWord.isEmpty()) {
            try {
                String[] wordsAsString = new String[resultWord.size()];
                int count = 0;
                for (Word word : resultWord) {
                    wordsAsString[count] = "~~~~~~~~ ~~~~~~\n" + word.asWord() + "\n";
                    count++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, wordsAsString);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, "Problem with displaying result because :" + e.getMessage());
                listView.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, new String[]{"No result"}));
            }
        } else {
            final String noResultFound = "No result found for:";
            Log.w(TAG, noResultFound + searchQuery);
            listView.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, new String[]{noResultFound + searchQuery}));
        }

    }
}
