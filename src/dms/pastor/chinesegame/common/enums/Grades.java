package dms.pastor.chinesegame.common.enums;

import android.content.Context;

import dms.pastor.chinesegame.R;

/**
 * Author Dominik Symonowicz
 * Created 02/11/2012
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public enum Grades {
    PERFECT(100),
    AWESOME(90),
    GREAT(80),
    GOOD(70),
    POOR(60),
    FAIL(0),
    EPIC_FAIL(Integer.MIN_VALUE);

    private final int percentage;

    Grades(int grade) {
        percentage = grade;
    }

    private static Grades getGradeForScore(int score) {
        if (score >= PERFECT.percentage) {
            return PERFECT;
        }

        if (score >= GREAT.percentage) {
            return GREAT;
        }

        if (score >= GOOD.percentage) {
            return GOOD;
        }

        if (score >= POOR.percentage) {
            return POOR;
        }
        if (score >= FAIL.percentage) {
            return FAIL;
        }
        return EPIC_FAIL;
    }

    public static String giveAGrade(Context context, int score) {
        switch (Grades.getGradeForScore(score)) {
            case PERFECT:
                return context.getResources().getString(R.string.perfect);
            case AWESOME:
                return context.getResources().getString(R.string.awesome);
            case GREAT:
                return context.getResources().getString(R.string.great);
            case GOOD:
                return context.getResources().getString(R.string.good);
            case POOR:
                return context.getResources().getString(R.string.poor);
            case FAIL:
                return context.getResources().getString(R.string.fail);
            default:
                return context.getResources().getString(R.string.epic_fail);
        }
    }

}
