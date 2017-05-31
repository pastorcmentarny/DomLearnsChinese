package dms.pastor.chinesegame.data.game.score;


import android.support.annotation.NonNull;
import android.util.Log;

import dms.pastor.chinesegame.Config;

/**
 * User: dominik symonowicz
 * Date: 16/11/12
 * Time: 10:23
 * This classes are response for everything relate to single score record
 */
public final class Score implements Comparable<Score> {
    private static final String TAG = "SCORE";
    private final String playerName;
    private final int score;
    private final int level;
    private final String data;
    private int gameNo = 0;
    private int gameVersion = 0;
    private long dateStamp = 0;
    private Difficulty difficulty = Difficulty.UNKNOWN;

    public Score() {
        this.playerName = Config.DEFAULT_USER_NAME;
        this.score = 0;
        this.level = 0;
        this.data = Config.DEFAULT_NO_DATE;
        this.gameNo = 0;
        this.gameVersion = 0;
        this.dateStamp = 0;
        this.difficulty = Difficulty.OLD;
    }

    public Score(String playerName, int score, int level, String data, int gameNo, int gameVersion, long dateStamp, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        this.data = data;
        this.gameNo = gameNo;
        this.gameVersion = gameVersion;
        this.dateStamp = dateStamp;
        try {
            this.difficulty = Difficulty.valueOf(difficulty);
        } catch (IllegalArgumentException iae) {
            Log.w(TAG, "Error: \'" + iae.getMessage() + "\' while getting difficulty.");
            this.difficulty = Difficulty.UNKNOWN;
        }

    }

    public Score(String playerName, int score, int level, String data, int gameNo, int gameVersion, long dateStamp) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        this.data = data;
        this.gameNo = gameNo;
        this.gameVersion = gameVersion;
        this.dateStamp = dateStamp;
        this.difficulty = Difficulty.OLD;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }


    public int getLevel() {
        return level;
    }


    public String getData() {
        return data;
    }


    @Override
    public String toString() {
        return Config.SPR + playerName + Config.SPR + score + Config.SPR + level + Config.SPR + data + Config.SPR + gameNo + Config.SPR + gameVersion + Config.SPR + dateStamp + Config.SPR + difficulty.name() + Config.SPR + "\n";
    }

    public String asHighScore() {
        return " Score:  " + score + " Date: " + data + "\n";
    }

    public String asHighScoreList() {
        return " Level: " + level + "\nDate: " + data + "\nGame no.: " + gameNo + "\nGame Version: " + gameVersion + "\nDateStamp: " + dateStamp + "\nDifficulty: " + difficulty.name() + "\n";
    }

    @Override
    public int compareTo(@NonNull Score score) {
        return score.getScore() - getScore();
    }
}
