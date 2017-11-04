package dms.pastor.chinesegame.data.dictionary;

import android.util.Log;

import static dms.pastor.chinesegame.Config.EMPTY_STRING;
import static dms.pastor.chinesegame.Config.NEW_LINE;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 25/09/2013
 * <p>
 * This contains question and answer and some  common methods to retrieve them.
 */
public final class QA {
    private static final String TAG = "QA";
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
                return question.toString() + NEW_LINE + answer.toString() + NEW_LINE;
            case ENGLISH:
                return question.getEnglish() + NEW_LINE + answer.getEnglish() + NEW_LINE;
            case CHINESE_CHARACTER:
                return question.getCharacter() + NEW_LINE + answer.getCharacter() + NEW_LINE;
            case CHINESE_PINYIN:
                return question.getPinyin() + NEW_LINE + answer.getPinyin() + NEW_LINE;
            case POLISH:
                return question.getPolish() + NEW_LINE + answer.getPolish() + NEW_LINE;
            default:
                Log.i(TAG, "Unknown QA type:" + type.name());
                return EMPTY_STRING;
        }
    }

    public enum QAType {
        CHINESE_CHARACTER, CHINESE_PINYIN, ENGLISH, POLISH, ALL
    }


}
