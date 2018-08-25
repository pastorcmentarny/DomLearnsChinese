package dms.pastor.chinesegame.data.game.score;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;

import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 18/11/2012
 */
public final class HighScoreList extends ListActivity {
    private static final String TAG = "High Score List";
    private HighScore highScore;
    private GameType gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String type = extras != null ? extras.getString("HS") : "NONE";
        highScore = HighScore.getHighScore();
        String[] scoreList = null;
        try {
            gameType = GameType.valueOf(type);
            scoreList = generateHighScore(gameType);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, "Unknown type of game");

            finish();
        }
        if (scoreList != null) {
            setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scoreList));
            getListView().setTextFilterEnabled(true);
        } else {
            Log.e(TAG, "Unable to generate scores as list for highscore");
            finish();
        }

    }

    private String[] generateHighScore(GameType gameType) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<Score> allScores = highScore.getScoresFor(gameType);
        if (allScores != null) {
            int counter = 0;
            for (Score score : allScores) {
                counter++;
                list.add(String.format(ENGLISH, "%s. %s %d pts. level: %d %s", String.valueOf(counter), String.valueOf(score.getPlayerName()), score.getScore(), score.getLevel(), score.getDate()));
            }

        } else {
            return new String[]{"No results"};
        }
        if (list.size() == 0) {
            list.add("No results.\n You need play a game first, before you can see high scores :)");
        }
        String[] listAsArray = new String[list.size()];
        return list.toArray(listAsArray);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Score score = highScore.getScoresFor(gameType).get(position);
        if (score != null) {
            final Dialog countryDialog = new Dialog(this);
            countryDialog.setContentView(R.layout.score_pop_up);
            countryDialog.setTitle(score.getPlayerName());
            TextView countryName = countryDialog.findViewById(R.id.countryName);
            countryName.setText(String.format("%s %s", String.valueOf(score.getScore()), getString(R.string.points)));
            TextView message = countryDialog.findViewById(R.id.countryDialogMessage);
            message.setText(score.asHighScoreList());

            Button dialogButton = countryDialog.findViewById(R.id.buttonOK);
            dialogButton.setOnClickListener(v1 -> countryDialog.dismiss());
            countryDialog.show();


        } else {
            Log.e(TAG, "unable to get country ");
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ERROR");
            dialog.setMessage("Program has problem with dictionary. It means Author(Me) mess up something.Please.send me an email. I am sorry for problem!\nPos:" + position);
            dialog.setNeutralButton(getResources().getString(R.string.awesome), (di, arg) -> {

            });
            dialog.show();
        }
    }


}