package dms.pastor.chinesegame.learning.dictionary;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.data.dictionary.Question;
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
public final class QuestionList extends ListActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = Config.TAG_PREFIX + "Question List";
    private String[] wordsList;
    private Clipboard4 clipboard;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dmManager = DatabaseManager.getDbManager(this);
        if (dmManager != null) {
            List<Question> questions = DatabaseManager.getDbService().getAllQuestions();
            wordsList = DatabaseManager.getQuestionAsWordList(questions);
        } else {
            finish();
        }
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        clipboard = new Clipboard4();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        StringBuilder wordBuilder = new StringBuilder(Config.EMPTY_STRING);
        final Question question = DatabaseManager.getDbService().getQuestion(position + 1);
        AlertDialog.Builder dialog;
        if (question != null) {
            wordBuilder.append(question.getCharacter()).append("\n");
            wordBuilder.append(question.getPinyin()).append("\n");
            wordBuilder.append(question.getEnglish()).append("\n");
            if (settings.getBoolean("polishMode", false)) {
                wordBuilder.append(question.getPolish()).append("\n");
            }
            wordBuilder.append(question.getNotes()).append("\n");
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.word_info_title));
            dialog.setMessage(wordBuilder.toString());
            dialog.setPositiveButton(getResources().getString(R.string.ok), (di, arg) -> {

            });
            dialog.setNeutralButton(getResources().getString(R.string.copy_to_clipboard), (di, arg) -> {
                clipboard.saveText(getApplicationContext(), question.getCharacter());
                Toast.makeText(getApplicationContext(), "Saved to clipboard", Toast.LENGTH_SHORT).show();
            });
            dialog.show();

        } else {
            Log.e(TAG, getString(R.string.e_get_word));
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.e));
            dialog.setMessage(getString(R.string.e_dictionary_problem_msg2me) + position);
            dialog.setNeutralButton(getResources().getString(R.string.ok), (di, arg) -> DomUtils.sorryToast(getApplicationContext()));
            dialog.show();
        }
    }

}
