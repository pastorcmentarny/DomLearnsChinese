package dms.pastor.chinesegame.data.learning.patterns;

import dms.pastor.chinesegame.utils.DomUtils;

/**
 * User: Pastor
 * Date: 07.04.13
 * Time: 02:51
 * A Sentence pattern entity
 */
public final class Pattern {
    private int id;
    private String title;
    private String description;
    private String patternExample;
    private String example1;
    private String example2;
    private String example3;
    private String example4;
    private String example5;

    public void setId(int _id) {
        this.id = _id;
    }

    public void setPatternExample(String patternExample) {
        this.patternExample = patternExample;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample1() {
        return example1;
    }

    public void setExample1(String example1) {
        this.example1 = example1;
    }

    public String getExample2() {
        return example2;
    }

/*
    public int getId() {
        return id;
    }
 */

    public void setExample2(String example2) {
        this.example2 = example2;
    }

    public String getExample3() {
        return example3;
    }

    public void setExample3(String example3) {
        this.example3 = example3;
    }

    public String getExample4() {
        return example4;
    }

    public void setExample4(String example4) {
        this.example4 = example4;
    }

    public String getExample5() {
        return example5;
    }

    public void setExample5(String example5) {
        this.example5 = example5;
    }

    private boolean validate() {
        return id >= 0 && !(DomUtils.isStringEmpty(title) || DomUtils.isStringEmpty(patternExample));

    }

    public boolean isValidated() {
        return validate();
    }


    @Override
    public String toString() {
        return "Pattern{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", patternExample='" + patternExample + '\'' +
                ", example1='" + example1 + '\'' +
                ", example2='" + example2 + '\'' +
                ", example3='" + example3 + '\'' +
                ", example4='" + example4 + '\'' +
                ", example5='" + example5 + '\'' +
                '}';
    }
}
