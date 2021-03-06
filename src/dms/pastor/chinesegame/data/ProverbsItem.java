package dms.pastor.chinesegame.data;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Email: email can be found on my website
 */
public final class ProverbsItem {
    private int id;
    private String chinese;
    private String pinyin;
    private String english;
    private String polish;
    private String meaning;
    private String notes;

    public ProverbsItem() {

    }

    public ProverbsItem(int id, String chinese, String pinyin, String english, String polish, String meaning, String notes) {
        this.id = id;
        this.chinese = chinese;
        this.pinyin = pinyin;
        this.english = english;
        this.polish = polish;
        this.meaning = meaning;
        this.notes = notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isValidated() {
        return id > 0;
        //return id >0 && DomUtils.isStringEmpty(chinese) && DomUtils.isStringEmpty(chinese);
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return format(ENGLISH, "ProverbsItem{id=%d, chinese='%s', pinyin='%s', english='%s', polish='%s', meaning='%s', notes='%s'}", id, chinese, pinyin, english, polish, meaning, notes);
    }
}
