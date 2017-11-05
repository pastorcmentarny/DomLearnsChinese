package dms.pastor.chinesegame.learning.dictionary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.db.DatabaseManager;


/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 18/09/2013
 */
public final class DictionaryList extends ListActivity {
    private static final String TAG = Config.TAG_PREFIX + "Dictionary List";
    private AlertDialog.Builder dialog;
    private Dictionary dictionary;
    private Clipboard4 clipboard;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dbManager = DatabaseManager.getDbManager(this);
        dictionary = dbManager.getDictionary();
        String[] wordsList = dictionary.generateWordList();
        clipboard = new Clipboard4();
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Word word = dictionary.getWordFromDictionary(position);

        if (word != null) {
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.word_info_title));
            dialog.setMessage(getWordAsString(word));
            dialog.setPositiveButton(getResources().getString(R.string.ok), (di, arg) -> {

            });
            dialog.setNeutralButton(getResources().getString(R.string.copy_to_clipboard), (di, arg) -> {
                clipboard.saveText(getApplicationContext(), word.getChineseCharacter());
                Toast.makeText(getApplicationContext(), "Saved to clipboard", Toast.LENGTH_SHORT).show();
            });
            dialog.show();

        } else {
            Log.e(TAG, getString(R.string.e_get_word));
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.e));
            dialog.setMessage(getString(R.string.e_dictionary_problem_msg2me) + position);
            dialog.setNeutralButton(getResources().getString(R.string.ok), (di, arg) -> {
            });
            dialog.show();
        }
    }

    public final boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dictmenu, menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dbStats:
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.dictionary_stats));
                DatabaseManager.getDbManager(getApplicationContext());
                dialog.setMessage(DatabaseManager.getDbService().displayInfoAboutDB());
                dialog.setNeutralButton(getResources().getString(R.string.ok), (di, arg) -> {
                    //NONE
                });
                dialog.show();
                return true;
            case R.id.wordStats:
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle(getString(R.string.dictionary_stats));
                dialog.setMessage(Dictionary.getDictionary().getWordsPerLevelStats());
                dialog.setNeutralButton(getResources().getString(R.string.ok), (di, arg) -> {
                    //NONE
                });
                dialog.show();
                return true;
            case R.id.findWords:
                Intent ii = new Intent(getApplicationContext(), WordFinder.class);
                startActivity(ii);
                return true;
            default:
                return false;
        }
    }

    private String getWordAsString(Word word) {
        StringBuilder wordBuilder = new StringBuilder("");
        wordBuilder.append(word.getChineseCharacter()).append("\n");
        wordBuilder.append(word.getPinyin()).append("\n");
        wordBuilder.append(getString(R.string.word_builder_stroke)).append(word.getStrokes() > 0 ? word.getStrokes() : "?").append("\n");
        wordBuilder.append(getString(R.string.word_builder_english)).append(word.getWordInEnglish()).append("\n");
        wordBuilder.append(getString(R.string.word_builder_notes)).append(word.getNotes() != null ? word.getNotes() : "").append("\n\n");
        String groups = "";
        if (word.getGroups() != null) {
            for (String group : word.getGroups()) {
                groups += group + ",";
            }
        } else {
            groups = "?";
        }
        wordBuilder.append(getString(R.string.word_builder_groups)).append(groups).append("\n");
        if (settings.getBoolean("polishMode", false)) {
            wordBuilder.append(getString(R.string.word_builder_polish)).append(word.getWordInPolish()).append("\n");
        }
        return wordBuilder.toString();
    }

}
