package dms.pastor.chinesegame.menu;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Locale;

import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.WordMistake;

/**
 * author: Pastor cmentarny
 * WWW: https://dominiksymonowicz.blogspot.co.uk
 * Github: https://github.com/pastorcmentarny
 * Google Play:
 * https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Email: email can be found on my website
 * <p/>
 * Created  18.09.13
 * To change this template use File | Settings | File Templates.
 */
public final class WordMistakesCounterView extends ListActivity {

    private static final String NO_RESULTS = "No results.";
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<WordMistake> errorWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Statistic statistic = Statistic.getStatistic(this);
        errorWordList = statistic.getSortedWordMistakes();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generateWordMistakesList()));
        getListView().setTextFilterEnabled(true);
    }

    private String[] generateWordMistakesList() {
        ArrayList<String> list = new ArrayList<>();
        if (errorWordList != null && errorWordList.size() > 0) {
            int counter = 0;
            for (WordMistake wordMistake : errorWordList) {
                counter++;
                list.add(String.format(Locale.ENGLISH, "%s. %s{%s} :%d", String.valueOf(counter), String.valueOf(wordMistake.getWord().getWordInEnglish()), wordMistake.getWord().getChineseCharacter(), wordMistake.getCounter()));
            }

        } else {
            return new String[]{NO_RESULTS};
        }
        if (list.size() == 0) {
            list.add(NO_RESULTS + "\n You need play a game first, before you can list of words :)");
        }
        String[] listAsArray = new String[list.size()];
        return list.toArray(listAsArray);
    }

}
