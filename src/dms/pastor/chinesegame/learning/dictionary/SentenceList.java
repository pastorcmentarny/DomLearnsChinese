package dms.pastor.chinesegame.learning.dictionary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.data.dictionary.Sentence;
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
public class SentenceList extends ListActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = "Sentence List";
    private List<Sentence> sentences;
    private Clipboard4 clipboard;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dbManager = DatabaseManager.getDbManager(this);
        if (dbManager != null) {
            sentences = DatabaseManager.getDbService().getAllSentences();
        }
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        String[] wordsList = DatabaseManager.getSentencesAsWordList(sentences);
        clipboard = new Clipboard4();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        StringBuilder wordBuilder = new StringBuilder("");
        final Sentence sentence = DatabaseManager.getDbService().getSentence(position + 1);
        AlertDialog.Builder dialog;
        if (sentence != null) {
            wordBuilder.append(sentence.getCharacter()).append("\n");
            wordBuilder.append(sentence.getPinyin()).append("\n");
            wordBuilder.append(sentence.getEnglish()).append("\n");
            wordBuilder.append(sentence.getNotes()).append("\n");
            if (settings.getBoolean("polishMode", false)) {
                wordBuilder.append(sentence.getPolish()).append("\n");
            }
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.word_info_title));
            dialog.setMessage(wordBuilder.toString());
            dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface di, final int arg) {

                }
            });
            dialog.setNeutralButton(getResources().getString(R.string.copy_to_clipboard), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface di, final int arg) {
                    clipboard.saveText(getApplicationContext(), sentence.getCharacter());
                    Toast.makeText(getApplicationContext(), "Saved to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();

        } else {
            Log.e(TAG, getString(R.string.e_get_word));
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.e));
            dialog.setMessage(getString(R.string.e_dictionary_problem_msg2me) + position);
            dialog.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface di, final int arg) {
                    DomUtils.sorryToast(getApplicationContext());
                }
            });
            dialog.show();
        }
    }

}
