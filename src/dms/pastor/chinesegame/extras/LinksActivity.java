package dms.pastor.chinesegame.extras;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.data.LinkItem;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Email: email can be found on my website
 * <p>
 * Created 07.06.2013
 */
public final class LinksActivity extends ListActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private static final String TAG = Config.TAG_PREFIX + "Links";
    private final DatabaseManager dbManager = DatabaseManager.getDbManager(this);
    private String[] wordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dbManager != null) {
            List<LinkItem> linksItems = DatabaseManager.getDbService().getAllLinks();
            wordsList = dbManager.getLinksAsWordList(linksItems);
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
        final LinkItem linkItem;
        try {
            linkItem = DatabaseManager.getDbService().getLink(position + 1);
            AlertDialog.Builder dialog;
            if (linkItem != null) {
                wordBuilder.append(linkItem.getWww()).append("\n");
                wordBuilder.append(linkItem.getNotes()).append("\n");
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle(linkItem.getTitle());
                dialog.setMessage(wordBuilder.toString());
                dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface di, final int arg) {
                    }
                });
                dialog.setNeutralButton(getResources().getString(R.string.go2www), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface di, final int arg) {
                        Intent browserIntent =
                                new Intent(Intent.ACTION_VIEW, Uri.parse(linkItem.getWww()));
                        startActivity(browserIntent);
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
