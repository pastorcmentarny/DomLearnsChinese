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

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 18/11/2012
 */
public final class VocabularyList extends ListActivity {
    private static final String TAG = "Vocabulary List";
    private Dictionary dictionary;
    private Clipboard4 clipboard;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        dictionary = Dictionary.getDictionary();
        String[] wordsList = dictionary.generateWordList();
        clipboard = new Clipboard4();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        StringBuilder wordBuilder = new StringBuilder(Config.EMPTY_STRING);
        final Word word = dictionary.getWordFromDictionary(position);
        AlertDialog.Builder dialog;
        if (word != null) {
            wordBuilder.append(word.getChineseCharacter()).append("\n");
            wordBuilder.append(word.getPinyin()).append("\n");
            wordBuilder.append(getString(R.string.word_builder_stroke)).append(word.getStrokes() > 0 ? word.getStrokes() : "?").append("\n");
            wordBuilder.append(getString(R.string.word_builder_english)).append(word.getWordInEnglish()).append("\n");
            wordBuilder.append(getString(R.string.word_builder_notes)).append(word.getNotes() != null ? word.getNotes() : Config.EMPTY_STRING).append("\n\n");
            StringBuilder groups = new StringBuilder(Config.EMPTY_STRING);
            if (word.getGroups() != null) {
                for (String group : word.getGroups()) {
                    groups.append(group).append(",");
                }
            } else {
                groups.append("?");
            }
            wordBuilder.append(getString(R.string.word_builder_groups)).append(groups).append("\n");
            if (settings.getBoolean("polishMode", false)) {
                wordBuilder.append(getString(R.string.word_builder_polish)).append(word.getWordInPolish()).append("\n");
            }
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.word_info_title));
            dialog.setMessage(wordBuilder.toString());
            dialog.setPositiveButton(getResources().getString(R.string.ok), (di, arg) -> {

            });
            dialog.setNeutralButton(getResources().getString(R.string.copy_to_clipboard), (di, arg) -> {
                final boolean saved = clipboard.saveText(getApplicationContext(), word.getChineseCharacter());
                String message;
                if (saved) {
                    message = getString(R.string.copied2clipboard);
                } else {
                    message = getString(R.string.error_unable_to_copy_to_clipboard);
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

/*
    private void setup(){
        switch(dictType){
            case WORDS:
                dictionary = Dictionary.getDictionary();
                dictionary.readDictionaryFromFile(getApplicationContext(), R.raw.dictionary, null);
                wordsList = dictionary.generateWordList();
                break;
            case RADICALS:
                radicalsDictionary = RadicalsDictionary.getDictionary(getApplicationContext());
                radicalsDictionary.readDictionaryFromFile(getApplicationContext());
                wordsList = radicalsDictionary.generateWordList();
                break;
            case QUESTIONS:
                dbService = DictUtils.getDatabaseService(this);
                if(dbService !=null){
                    questions = dbService.getAllQuestions();
                    wordsList = DictUtils.getQuestionAsStringArray();
                }  else {
                    finish();
                }

            break;
            case SENTENCES:
                if(dbService !=null){
                dbService = DictUtils.getDatabaseService(this);
        }  else {
            finish();
        }
                sentences = dbService.getAllSentences();
                wordsList = DictUtils.getSentencesAsStringArray();
                break;
            default:
                DomUtils.displayError(this,"Dictionary selection is wrong.");
                finish();
        }

    }
 */


}