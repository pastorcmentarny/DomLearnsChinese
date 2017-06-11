package dms.pastor.chinesegame.common.enums;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 */
public enum Stage {
    STAGE8(175, 8, 8),
    STAGE7(140, 7, 6),
    STAGE6(110, 6, 5),
    STAGE5(85, 5, 4),
    STAGE4(60, 4, 3),
    STAGE3(40, 3, 2),
    STAGE2(20, 2, 1.5),
    STAGE1(0, 1, 1);
    private final int level;
    private final int difficulty;
    private final double scoreBonusMultiply;

    Stage(int stage, int difficulty, double scoreBonusMultiply) {
        this.level = stage;
        this.difficulty = difficulty;
        this.scoreBonusMultiply = scoreBonusMultiply;
    }

    public static int getDifficultyLevelForLevel(int level) {
        if (level > STAGE8.level) {
            return STAGE8.difficulty;
        } else if (level > STAGE7.level) {
            return STAGE7.difficulty;
        } else if (level > STAGE6.level) {
            return STAGE6.difficulty;
        } else if (level > STAGE5.level) {
            return STAGE5.difficulty;
        } else if (level > STAGE4.level) {
            return STAGE4.difficulty;
        } else if (level > STAGE3.level) {
            return STAGE3.difficulty;
        } else if (level > STAGE2.level) {
            return STAGE2.difficulty;
        } else {
            return STAGE1.difficulty;
        }
    }

    public static Stage getStageForLevel(int level) {
        if (level > STAGE8.level) {
            return STAGE8;
        } else if (level > STAGE7.level) {
            return STAGE7;
        } else if (level > STAGE6.level) {
            return STAGE6;
        } else if (level > STAGE5.level) {
            return STAGE5;
        } else if (level > STAGE4.level) {
            return STAGE4;
        } else if (level > STAGE3.level) {
            return STAGE3;
        } else if (level > STAGE2.level) {
            return STAGE2;
        } else {
            return STAGE1;
        }
    }

    public double getScoreBonusMultiply() {
        return scoreBonusMultiply;
    }

    public int getDifficulty() {
        return difficulty;
    }

}
