package dms.pastor.chinesegame.extras;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.data.GrammarTipItem;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 07/06/2013
 */
public final class LearningTipsActivity extends ListActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = "Learning Tips List";
    private String[] wordsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager dbManager = DatabaseManager.getDbManager(this);
        if (dbManager != null) {
            List<GrammarTipItem> grammarTipItems = DatabaseManager.getDbService().getAllGrammarTips();
            wordsList = dbManager.getGrammarTipsAsWordList(grammarTipItems);
        } else {
            finish();
        }

        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        StringBuilder wordBuilder = new StringBuilder("");
        GrammarTipItem cultureInfoItem = null;
        try {
            cultureInfoItem = DatabaseManager.getDbService().getGrammarTip(position + 1);
        } catch (NotFoundException e) {
            Log.w(TAG, String.format("Culture info not found for item in position %d", position + 1));
        }
        AlertDialog.Builder dialog;
        if (cultureInfoItem != null) {
            wordBuilder.append(cultureInfoItem.getInfo()).append("\n");
            wordBuilder.append(cultureInfoItem.getNotes()).append("\n");
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(cultureInfoItem.getTitle());
            dialog.setMessage(wordBuilder.toString());
            dialog.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface di, final int arg) {

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
