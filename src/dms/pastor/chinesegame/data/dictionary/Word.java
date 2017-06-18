package dms.pastor.chinesegame.data.dictionary;

import android.util.Log;

import static dms.pastor.chinesegame.utils.DomUtils.isStringEmpty;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 14/11/2012
 */
public final class Word {
    private static final String TAG = "WORD";
    private int id;
    private String chineseCharacter;
    private String pinyin;
    private int strokes;
    private String wEnglish;
    private String wPolish;
    private String[] groups;
    private String notes;
    private int difficulty;

    public Word(int id, String chineseCharacter, String pinyin, int strokes, String wEnglish, String wPolish, String[] groups, String notes, int difficulty) {
        setId(id);
        setChineseCharacter(chineseCharacter);
        setPinyin(pinyin);
        setStrokes(strokes);
        setWordInEnglish(wEnglish);
        setWordInPolish(wPolish);
        setGroups(groups);
        setNotes(notes);
        setDifficulty(difficulty);
    }

    public Word noWord() {
        return new Word(-1, null, null, -1, null, null, null, null, -1);
    }


    public String getChineseCharacter() {
        return chineseCharacter;
    }

    private void setChineseCharacter(String chineseCharacter) {
        this.chineseCharacter = chineseCharacter;
    }

    public int getDifficulty() {
        return difficulty;
    }

    private void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getPinyin() {
        return pinyin;
    }

    private void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getWordInEnglish() {
        return wEnglish;
    }

    private void setWordInEnglish(String wEnglish) {
        this.wEnglish = wEnglish;
    }

    public String getWordInPolish() {
        return wPolish;
    }

    private void setWordInPolish(String wPolish) {
        this.wPolish = wPolish;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getStrokes() {
        return strokes;
    }

    private void setStrokes(int strokes) {
        this.strokes = strokes;
    }


    //TODO improve validation
    boolean isValid() {

        if (id < 0) {
            Log.e(TAG, "id is invalid.value:" + id);
            return false;
        }

        if (strokes < -1) {
            Log.e(TAG, "stroke is invalid.value:" + id);
            return false;
        }

        if (isStringEmpty(chineseCharacter)) {
            Log.e(TAG, "Chinese character is invalid:" + id);
            return false;
        }

        if (isStringEmpty(pinyin)) {
            return false;
        }

        if (isStringEmpty(wEnglish)) {
            Log.e(TAG, "English word is invalid:" + id);
            return false;
        }

        if (isStringEmpty(wPolish)) {
            Log.e(TAG, "English word is invalid:" + id);
            return false;
        }

        if (isStringEmpty(notes)) {
            Log.e(TAG, "Notes  is invalid:" + id);
            return false;
        }

        return true;
    }


    public String[] getGroups() {
        return groups;
    }

    private void setGroups(String[] groups) {
        this.groups = groups;
    }

    public String getNotes() {
        return notes;
    }

    private void setNotes(String notes) {
        this.notes = notes;
    }


    @Override //TODO change this toShortString
    public String toString() {
        return chineseCharacter + " - '" +
                pinyin + "' - [ " +
                wEnglish + " ]";

    }

    public String asWord() {
        return "ID: " + id + "\nChinese: " + chineseCharacter + "\nPinyin: " + pinyin + "(" + strokes + ")\nEnglish: " + wEnglish + "\nPolish:" + wPolish + "\nNotes: " + notes + "\nDifficulty: " + difficulty;
    }

}
