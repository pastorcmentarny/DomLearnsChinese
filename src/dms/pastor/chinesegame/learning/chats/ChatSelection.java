package dms.pastor.chinesegame.learning.chats;

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
import dms.pastor.chinesegame.common.exceptions.NullListException;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.learning.chats.Chat;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.utils.DomUtils;

import static dms.pastor.chinesegame.db.DatabaseManager.getDbManager;
import static dms.pastor.chinesegame.db.DatabaseManager.getDbService;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 05/01/2013
 */
public final class ChatSelection extends ListActivity {
    private static final String TAG = "CHAT SELECTION";
    private DatabaseManager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "creating..");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dbManager = getDbManager(this);
        if (databaseIsNotNull()) {
            try {
                setupChatSelectionList();
                setupSelectionElementForList();
            } catch (NullListException | NotFoundException e) {
                error(e.getMessage());
            }
        }
    }

    private void setupChatSelectionList() throws NotFoundException, NullListException {
        List<Chat> chats = getDbService().getAllChats();
        String[] chatList = dbManager.getChatsAsWordList(chats);
        setListAdapter(new ArrayAdapter<>(this, R.layout.selection_list, chatList));
    }

    private void setupSelectionElementForList() {
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String chatTitle = ((TextView) view).getText().toString();
                Chat chat = null;
                try {
                    chat = dbManager.findChatByTitle(chatTitle);
                } catch (NotFoundException e) {
                    error(e.getMessage());
                }

                Intent ii = new Intent(getApplicationContext(), ChatLesson.class);
                setupPlayer(chat);
                startActivity(ii);
            }
        });
    }

    private boolean databaseIsNotNull() {
        return dbManager != null && getDbService() != null;
    }

    private static void setupPlayer(Chat chat) {
        Player player = Player.getPlayer();
        player.setCurrentChat(chat);
    }

    private void error(String message) {
        DomUtils.displayError(this, message);
        finish();
    }

}
