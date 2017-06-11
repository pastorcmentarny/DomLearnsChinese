package dms.pastor.chinesegame.data.dictionary;

import static dms.pastor.chinesegame.Config.EMPTY_STRING;
import static dms.pastor.chinesegame.utils.DomUtils.isStringEmpty;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 29/08/2013
 */
public final class Question {
    private int id;
    private String character;
    private String pinyin;
    private String polish;
    private String english;
    private String notes;

    public Question() {
    }


    public static Question getEmptyQuestion() {
        Question question = new Question();
        question.setId(0);
        question.setCharacter(EMPTY_STRING);
        question.setPinyin(EMPTY_STRING);
        question.setPolish(EMPTY_STRING);
        question.setEnglish(EMPTY_STRING);
        question.setNotes(EMPTY_STRING);
        return question;
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
        return id >= 0 && !(isStringEmpty(character) || isStringEmpty(pinyin) || isStringEmpty(english));
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
