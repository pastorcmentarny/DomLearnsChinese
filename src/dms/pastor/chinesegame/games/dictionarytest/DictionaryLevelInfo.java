package dms.pastor.chinesegame.games.dictionarytest;

import android.util.Log;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 */
public enum DictionaryLevelInfo {
    STAGE1(1, 8, 10, 2),
    STAGE2(2, 12, 12, 4),
    STAGE3(3, 19, 16, 6),
    STAGE4(4, 18, 20, 9),
    STAGE5(5, 14, 26, 12),
    STAGE6(6, 12, 32, 15),
    STAGE7(7, 9, 40, 20),
    STAGE8(8, 8, 50, 30);

    private static final String TAG = "Dictionary Level Info";
    private final int level;
    private final int stageSize;
    private final int points;
    private final int penalty;

    DictionaryLevelInfo(int level, int stageSize, int points, int penalty) {
        this.level = level;
        this.stageSize = stageSize;
        this.points = points;
        this.penalty = penalty;
    }

    public static int getStageSizeForStage(int stage) {
        for (DictionaryLevelInfo dictionaryLevelInfo : DictionaryLevelInfo.values()) {
            if (dictionaryLevelInfo.getLevel() == stage) {
                return dictionaryLevelInfo.getStageSize();
            }
        }
        Log.wtf(TAG, "Bug detected! getStageSizeForStage() in DictionaryLevelInfo");
        return -1;
    }

    public static int getScoreForLevel(int difficulty) {
        for (DictionaryLevelInfo dictionaryLevelInfo : DictionaryLevelInfo.values()) {
            if (dictionaryLevelInfo.getLevel() == difficulty) {
                return dictionaryLevelInfo.getPoints();
            }
        }
        Log.wtf(TAG, "Bug detected! getScoreForLevel() in DictionaryLevelInfo");
        return -1;
    }

    public static int getPenaltyForLevel(int difficulty) {
        for (DictionaryLevelInfo dictionaryLevelInfo : DictionaryLevelInfo.values()) {
            if (dictionaryLevelInfo.getLevel() == difficulty) {
                return dictionaryLevelInfo.getPenalty();
            }
        }
        Log.wtf(TAG, "Bug detected! getPenaltyForLevel() in DictionaryLevelInfo");
        return -1;
    }

    public static String getAGrade(int score) {
        int lvl2 = STAGE1.getStageSize() * STAGE1.getPoints() + STAGE2.getPoints();
        int lvl3 = (STAGE2.getStageSize() * STAGE2.getPoints()) + lvl2 - STAGE2.getPoints() + STAGE3.getPoints();
        int lvl4 = (STAGE3.getStageSize() * STAGE3.getPoints()) + lvl3 - STAGE3.getPoints() + STAGE4.getPoints();
        int lvl5 = (STAGE4.getStageSize() * STAGE4.getPoints()) + lvl4 - STAGE4.getPoints() + STAGE5.getPoints();
        int lvl6 = (STAGE5.getStageSize() * STAGE5.getPoints()) + lvl5 - STAGE5.getPoints() + STAGE6.getPoints();
        int lvl7 = (STAGE6.getStageSize() * STAGE6.getPoints()) + lvl6 - STAGE6.getPoints() + STAGE7.getPoints();
        int lvl8 = (STAGE7.getStageSize() * STAGE7.getPoints()) + lvl7 - STAGE7.getPoints() + STAGE8.getPoints();
        Log.i(TAG, "level2: " + lvl2);
        Log.i(TAG, "level3: " + lvl3);
        Log.i(TAG, "level4: " + lvl4);
        Log.i(TAG, "level5: " + lvl5);
        Log.i(TAG, "level6: " + lvl6);
        Log.i(TAG, "level7: " + lvl7);
        Log.i(TAG, "level8: " + lvl8);


        if (score >= lvl8) {
            return "Native 8/8 ";
        } else if (score >= lvl7) {
            return "Proficiency (7/8)";
        } else if (score >= lvl6) {
            return "Fluent 6/8";
        } else if (score >= lvl5) {
            return "Advanced 5/8";
        } else if (score >= lvl4) {
            return "Intermediate 4/8";
        } else if (score >= lvl3) {
            return "Elementary 3/8";
        } else if (score >= lvl2) {
            return "Basic (2/8)";
        } else {
            return "None (1/8)";
        }
    }

    private int getLevel() {
        return level;
    }

    private int getStageSize() {
        return stageSize;
    }

    private int getPoints() {
        return points;
    }

    private int getPenalty() {
        return penalty;
    }
}
