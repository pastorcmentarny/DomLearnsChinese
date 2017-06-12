package dms.pastor.chinesegame.data.dictionary;

import dms.pastor.chinesegame.utils.DomUtils;

import static dms.pastor.chinesegame.Config.EMPTY_STRING;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 01/09/2013
 */
public final class Sentence {
    private int id;
    private String character;
    private String pinyin;
    private String polish;
    private String english;
    private String notes;

    public Sentence() {
    }


    public static Sentence getEmptySentence() {
        Sentence sentence = new Sentence();
        sentence.setId(0);
        sentence.setCharacter(EMPTY_STRING);
        sentence.setPinyin(EMPTY_STRING);
        sentence.setEnglish(EMPTY_STRING);
        sentence.setNotes(EMPTY_STRING);
        return sentence;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    private boolean validate() {
        return id >= 0 && !(DomUtils.isStringEmpty(character) || DomUtils.isStringEmpty(pinyin) || DomUtils.isStringEmpty(english));

    }

    public boolean isValidated() {
        return validate();
    }

    @Override
    public String toString() {
        return "\t" + character + '\n' +
                pinyin + '\n' +
                english + '\n';
    }

}
