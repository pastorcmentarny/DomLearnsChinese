package dms.pastor.chinesegame.data;

/**
 * Created with IntelliJ IDEA.
 * User: Pastor
 * Date: 17.09.13
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 */
public final class GrammarTipItem {
    private int id;
    private String title;
    private String info;
    private String notes;

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
        return id > 0;
    }
}
