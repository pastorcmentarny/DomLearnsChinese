package dms.pastor.chinesegame.data.dictionary;

import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Pastor
 * Date: 01.09.13
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */
public final class Sentence {
    private int id;
    private String character;
    private String pinyin;
    private String polish;
    private String english;
    private String notes;

    public Sentence() {
        //Nothing to do :(
    }


    public static Sentence getEmptySentence() {
        Sentence sentence = new Sentence();
        sentence.setId(0);
        sentence.setCharacter("");
        sentence.setPinyin("");
        sentence.setEnglish("");
        sentence.setNotes("");
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
