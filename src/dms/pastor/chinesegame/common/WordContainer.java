package dms.pastor.chinesegame.common;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 */
public final class WordContainer {
    private static final String TAG = "WordContainer";
    private static WordContainer wordContainer;
    private Random random;
    private ArrayList<Word> words;
    private Word word;

    private WordContainer() {
        initContainer();
    }

    public static synchronized WordContainer getWordContainer() {

        if (wordContainer == null) {
            wordContainer = new WordContainer();
        }
        return wordContainer;
    }

    private void initContainer() {
        random = new Random();
        words = Dictionary.getDictionary().getWordsList();
        word = words.get(random.nextInt(words.size()));
    }

    public Word nextRandomWord() {
        word = words.get(random.nextInt(words.size()));
        Log.d(TAG, " next random word is: " + word.getWordInEnglish());
        return word;
    }

    public Word getCurrentWord() {
        if (word == null) {
            word = nextRandomWord();
        }
        return word;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Object clone() throws CloneNotSupportedException {
        Log.e(TAG, "Clone not supported exception!");
        throw new CloneNotSupportedException();
    }
}
