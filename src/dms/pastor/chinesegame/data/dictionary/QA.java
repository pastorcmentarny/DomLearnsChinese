package dms.pastor.chinesegame.data.dictionary;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 25/09/2013
 * <p>
 * This contains question and answer and some  common methods to retrieve them.
 */
public final class QA {
    private Question question;
    private Sentence answer;

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setAnswer(Sentence answer) {
        this.answer = answer;
    }

    @SuppressWarnings("SameParameterValue")
    public String getQAas(QAType type) {
        switch (type) {
            case ALL:
                return question.toString() + "\n" + answer.toString() + "\n";
            case ENGLISH:
                return question.getEnglish() + "\n" + answer.getEnglish() + "\n";
            case CHINESE_CHARACTER:
                return question.getCharacter() + "\n" + answer.getCharacter() + "\n";
            case CHINESE_PINYIN:
                return question.getPinyin() + "\n" + answer.getPinyin() + "\n";
            case POLISH:
                return question.getPolish() + "\n" + answer.getPolish() + "\n";
            default:
                return "";
        }
    }

    public enum QAType {
        CHINESE_CHARACTER, CHINESE_PINYIN, ENGLISH, POLISH, ALL
    }


}
