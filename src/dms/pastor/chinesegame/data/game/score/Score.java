package dms.pastor.chinesegame.data.game.score;


import android.util.Log;
import dms.pastor.chinesegame.Config;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 16/11/2012
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
