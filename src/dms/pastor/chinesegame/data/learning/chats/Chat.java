package dms.pastor.chinesegame.data.learning.chats;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dms.pastor.chinesegame.data.dictionary.QA;
import dms.pastor.chinesegame.data.dictionary.Word;

import static dms.pastor.chinesegame.utils.DomUtils.isStringEmpty;

/**
 * Author: Pastor cmentarny
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Email: email can be found on my website
 * <p/>
 * Date: 22.09.13
 * Time: 00:23
 * <p/>
 * A single Chat
 */
public final class Chat {
    private static final String TAG = "CHAT";
    private int id;
    private String title;
    private String information;
    private List<QA> qaList;
    private List<Word> wordList;

    public Chat() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    private boolean validate() {
        return id >= 0 && !(isStringEmpty(title) || isStringEmpty(information));
    }

    public boolean isValidated() {
        return validate();
    }

    public void setQaList(List<QA> qaList) {
        this.qaList = qaList;
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    public List<String> getSentencesList() {
        ArrayList<String> sentencesList = new ArrayList<>();
        Log.i(TAG, "Generating sentence list");
        for (QA qa : qaList) {
            sentencesList.add(qa.getQAas(QA.QAType.ALL));
        }
        return sentencesList;
    }

    //TODO simplify  as it duplicate of   Dictionary's generateWordList()
    public String[] getWordsAsArray() {
        Log.i(TAG, "Generating word list");
        ArrayList<String> words = new ArrayList<>();
        for (Word word : wordList) {
            words.add(word.toString());
        }
        return words.toArray(new String[words.size()]);
    }
}
