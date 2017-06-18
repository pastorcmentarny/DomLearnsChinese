package dms.pastor.chinesegame.extras;

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

import java.util.List;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.data.ProverbsItem;
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
public final class ProverbsActivity extends ListActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = Config.TAG_PREFIX + "Proverbs";
    private final DatabaseManager dbManager = DatabaseManager.getDbManager(this);
    private String[] wordsList;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dbManager != null) {
            List<ProverbsItem> proverbsItems = DatabaseManager.getDbService().getAllProverbs();
            wordsList = dbManager.getProverbsAsWordList(proverbsItems);
        } else {
            finish();
        }
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        StringBuilder wordBuilder = new StringBuilder("");
        final ProverbsItem proverbsItem;
        try {
            proverbsItem = DatabaseManager.getDbService().getProverb(position + 1);
            AlertDialog.Builder dialog;
            if (proverbsItem != null) {
                wordBuilder.append("Chinese:")
                        .append(proverbsItem.getChinese())
                        .append("\n")
                        .append("Pinyin:")
                        .append(proverbsItem.getPinyin())
                        .append("\n")
                        .append("Meaning:")
                        .append(proverbsItem.getMeaning())
                        .append("\n");

                if (proverbsItem.getNotes() != null && !proverbsItem.getNotes().isEmpty()) {
                    wordBuilder.append("Meaning:")
                            .append(proverbsItem.getNotes())
                            .append("\n");
                }

                if (settings.getBoolean("polishMode", false)) {
                    wordBuilder.append("Polish:")
                            .append(proverbsItem.getPolish())
                            .append("\n");
                }

                dialog = new AlertDialog.Builder(this);
                dialog.setTitle(proverbsItem.getEnglish());
                dialog.setMessage(wordBuilder.toString());
                dialog.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
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
        } catch (NotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
