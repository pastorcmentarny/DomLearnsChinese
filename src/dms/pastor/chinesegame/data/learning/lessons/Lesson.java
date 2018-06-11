package dms.pastor.chinesegame.data.learning.lessons;

import java.util.ArrayList;
import java.util.Arrays;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.data.dictionary.Word;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Email: email can be found on my website
 * <p>
 * Created: 03.01'2013
 */
public final class Lesson {

    private int id = Integer.MIN_VALUE;
    private String title;
    private String[] group;
    private ArrayList<Word> words;
    private String lessonContent;
    private String notes;

    public int getId() {
        return id;
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

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public String getLessonContent() {
        return lessonContent;
    }

    public void setLessonContent(String lessonContent) {
        this.lessonContent = lessonContent;
    }

    public String[] getGroup() {
        return group;
    }

    public void setGroup(String[] group) {
        this.group = group;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isValidated() {
        return id > 0 && title != null && group != null;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", group=" + (group == null ? null : Arrays.asList(group)) +
                ", words=" + words +
                ", lessonContent='" + lessonContent + '\'' +
                (notes != null ? ", notes=" + notes : Config.EMPTY_STRING) +
                '}';
    }
}