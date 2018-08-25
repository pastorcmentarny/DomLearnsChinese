package dms.pastor.chinesegame.data.game.score;


import android.util.Log;

import dms.pastor.chinesegame.Config;

import static java.util.Locale.ENGLISH;

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
    private final String date;
    private final int gameNo;
    private final int gameVersion;
    private final long dateStamp;
    private Difficulty difficulty;

    public Score() {
        this.playerName = Config.DEFAULT_USER_NAME;
        this.score = 0;
        this.level = 0;
        this.date = Config.DEFAULT_NO_DATE;
        this.gameNo = 0;
        this.gameVersion = 0;
        this.dateStamp = 0;
        this.difficulty = Difficulty.OLD;
    }

    public Score(String playerName, int score, int level, String date, int gameNo, int gameVersion, long dateStamp, String difficulty) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        this.date = date;
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


    public String getDate() {
        return date;
    }


    @Override
    public String toString() {
        return Config.SPR + playerName + Config.SPR + score + Config.SPR + level + Config.SPR + date + Config.SPR + gameNo + Config.SPR + gameVersion + Config.SPR + dateStamp + Config.SPR + difficulty.name() + Config.SPR + "\n";
    }

    public String asHighScore() {
        return String.format(ENGLISH, " Score:  %d Level: %d\nDifficulty: %sGame Version: %d\n", score, level, difficulty.name(), gameVersion);
    }

    public String asHighScoreList() {
        return String.format(ENGLISH, " Level: %d\nDate: %s\nGame no.: %d\nGame Version: %d\nDateStamp: %d\nDifficulty: %s\n", level, date, gameNo, gameVersion, dateStamp, difficulty.name());
    }

    @Override
    public int compareTo(Score score) {
        return score.getScore() - getScore();
    }
}
