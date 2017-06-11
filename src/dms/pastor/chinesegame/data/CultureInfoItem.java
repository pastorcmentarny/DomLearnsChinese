package dms.pastor.chinesegame.data;

import dms.pastor.chinesegame.Config;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 17/09/2013
 */
public final class CultureInfoItem {
    private int id;
    private String title;
    private String info;
    private String notes;


    public CultureInfoItem() {
    }

    public CultureInfoItem(int id, String title, String info, String notes) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.notes = notes;
    }

    public static CultureInfoItem getEmptyItem() {
        return new CultureInfoItem(0, Config.EMPTY, Config.EMPTY, Config.EMPTY);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValidated() {
        return id >= 0;
    }
}
