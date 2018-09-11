package dms.pastor.chinesegame;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Game;
import dms.pastor.chinesegame.data.game.Player;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 16/11/2012
 * <p>
 * IMPORTANT: if you add anything to Config ,please add to displayConfig too!
 */
public final class Config {

    //Ads stuff
    public static final String AD_INTERSTITIAL_UNIT = "ca-app-pub-1669938002445825/2171184702";

    //time constants
    public static final int SECONDS = 1000;  // 1000 milliseconds

    //HSK
    public static final long HSK_BASIC_TIME_LIMIT = 240 * SECONDS;

    //constant
    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final int DEFAULT_BONUS_POINTS = 50;  //it based +1/100 words in wordList
    public static final int HEALTH_BONUS_PER_LEVEL = 1;
    public static final int MANA_BONUS_PER_LEVEL = 3;
    public static final int DEFAULT_FAIL_POINTS = 2;

    //misc settings
    public static final int HIGH_SCORE_SIZE = 500;
    public static final String HIGH_SCORE_FILE_PATH = "highscore.txt";
    public static final String HIGH_SCORE_SAPPER_FILE_PATH = "highscore_sapper.txt";
    public static final String HIGH_SCORE_DICTIONARY_FILE_PATH = "highscore_dictionary.txt";
    public static final long VIBRATE_ON_MISTAKE_TIME = 100;
    public static final int BONUS_POINTS_BASE = 10;
    public static final String TAG_PREFIX = "DLC ";
    public static final String DEFAULT_USER_NAME = "Chao";
    public static final int HEAL_HP_VALUE = 11;
    public static final long REFRESH = 200;
    public static final int RANDOM_EVENT_FREQ = 5;
    public static final boolean DEFAULT_PLAY_SOUND = false;
    public static final boolean DEFAULT_VIBRATE = true;
    public static final boolean DEFAULT_INTRO = true;
    public static final boolean DEFAULT_SHOW_PINYIN_SAPPER = false;
    public static final String DEFAULT_NO_DATE = "Ancient times";
    public static final int BONUS_ONLY_PINYIN = 50;
    public static final int REGEN_HP_VALUE = 4;
    public static final int TURN_RANGE = 10;
    public static final int HSK_BASIC_LEVELS_SIZE = 50;
    public static final int RANDOM_SIZE = 200;
    public static final int LESSON_LEVELS_SIZE = 10;
    public static final int FREEZE_RANGE = 50;

    //SPELLS cost
    public static final int PINYIN_SPELL_COST = 9;
    public static final int REMOVE_BAD_ANSWER_SPELL_COST = 11;
    public static final int HEAL_SPELL_COST = 23;
    public static final int CURE_SPELL_COST = 31;
    public static final String WORD_PREFIX_FOR_STATS = "word_";
    public static final String CIPHER_TYPE = "AES";
    public static final int NO_PENALTY_TIME = 4;
    public static final int ADVENTURE_NO_PENALTY_TIME = 4;
    public static final int SAPPER_NO_PENALTY_TIME = NO_PENALTY_TIME * 2;
    public static final int COMBO_MINIMUM = 3;
    public static final int REGEN_MANA_VALUE = 4 + new Random().nextInt(9);
    public static final int POISON_VALUE = 3 + new Random().nextInt(10);

    //DIFFICULTY RANGE FOR ADVENTURE
    //SCORE
    public static final int BONUS_POINTS_UNFREEZE = 3;
    public static final String SPR = ";;";

    //COMMON TEXT
    public static final boolean DEFAULT_SHOW_POLISH = false;
    public static final long NEXT_WORD_TIME = 5000;
    public static final File BACKUP_FOLDER = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/domsBackups");
    public static final int DICTIONARY_TEST_LEVELS_SIZE = 100;
    public static final int DICTIONARY_TEST_TIME_LIMIT = 300;
    public static final int HIDE_PINYIN_ON_EASY_ON_WORD_DIFFICULTY = 6;

    //STRING (Experimental)
    public static final String EMPTY_STRING = "";
    public static final String NEW_LINE = "\n";
    public static final String IGNORED_WORD = "////";
    public static final String COLUMN_SEPARATOR = ";;";
    public static final String GROUP_SEPARATOR = "~~";
    static final String APP_NAME = "dms.pastor.chinesegame";
    static final String MY_EMAIL = "dmspastor@gmail.com";
    private static final long HSK_BASIC_BONUS_TIME_UNIT = 30 * SECONDS;
    private static final String STATISTIC_FILENAME = "stats.txt";
    public static final File STATISTIC_FILE = new File(Config.BACKUP_FOLDER, Config.STATISTIC_FILENAME);
    private static final String TAG = TAG_PREFIX + " Config";
    private static final int JACKPOT_BONUS = 1000;
    private static final int MAX_PENALTY = 20;

    private Config() {
    }

    //CAMPAIGN
    public static int increaseFail(int fail) {
        int newFail = (2 * fail) + 1;
        return newFail > MAX_PENALTY ? MAX_PENALTY : newFail;
    }

    public static int reduceFail(int fail) {
        int newFail = fail - Math.round(MAX_PENALTY / 5);
        return newFail < DEFAULT_FAIL_POINTS ? DEFAULT_FAIL_POINTS : newFail;
    }

    public static int calcPenaltyHealthForTime(long time) {
        return (int) time / 1000;
    }

    private static int calcDictionarySizeBonus(int dictSize) {
        int bonus = (dictSize / 75) - 1;
        return bonus >= 0 ? bonus : 0;
    }

    public static int calculateScore() {
        Player player = Player.getPlayer();
        int score;
        score = player.getGame().getCorrect() * 100 / player.getGame().getLevels();
        score -= player.getGame().getMistake();
        if ((player.getGame().getStopTime() - player.getGame().getStartTime()) > HSK_BASIC_TIME_LIMIT) {
            score += calculateTime(player) / SECONDS;
        } else {
            score += calculateTime(player) / HSK_BASIC_BONUS_TIME_UNIT;
        }
        return score;
    }

    public static String getCurrentDateAsString() {
        return new SimpleDateFormat(DATE_FORMAT, ENGLISH).format(new Date());
    }

    public static String getFilePathFor(GameType gameType) {
        switch (gameType) {
            case ADVENTURE:
                return HIGH_SCORE_FILE_PATH;
            case SAPPER:
                return HIGH_SCORE_SAPPER_FILE_PATH;
            default:
                Log.e(TAG, "no file path for highscore : " + gameType.toString());
                return EMPTY_STRING;
        }
    }

    @SuppressWarnings("SpellCheckingInspection") //it contains key inside
    public static String getKey() {
        return "4q3ONmchPdv0Nfus4q4Pjs7P2PE6fr8Mk5e9WlOq5vCe2vrhvjZGvcOi8aIISZd".substring(0, 16);
    }

    @SuppressWarnings("SpellCheckingInspection") // it contains key inside
    public static byte[] getKeyAsBytes() throws UnsupportedEncodingException {
        return "4q3ONmchPdv0Nfus4q4Pjs7P2PE6fr8Mk5e9WlOq5vCe2vrhvjZGvcOi8aIISZd".substring(0, 16).getBytes("UTF-8");
    }

    public static String displayConfig() {
        return format(ENGLISH, "HSK_BASIC_TIME_LIMIT - %d\nDEFAULT_BONUS_POINTS - %d\nHEALTH_BONUS_PER_LEVEL" +
                        " - %d\nMANA_BONUS_PER_LEVEL - %d\nDEFAULT_FAIL_POINTS - %d\nHIGH_SCORE_SIZE - %d\nVIBRATE_ON_MISTAKE_TIME" +
                        " - %d\nBONUS_POINTS_BASE - %d\nHEAL_HP_VALUE - %d\nREFRESH - %d\nRANDOM_EVENT_FREQ" +
                        " - %d\nBONUS_ONLY_PINYIN - %d\nREGEN_HP_VALUE - %d\nTURN_RANGE - %d\nHSK_BASIC_LEVELS_SIZE" +
                        " - %d\nRANDOM_SIZE - %d\nLESSON_LEVELS_SIZE - %d\nFREEZE_RANGE - %d\nPINYIN_SPELL_COST" +
                        " - %d\nREMOVE_BAD_ANSWER_SPELL_COST" +
                        " - %d\nHEAL_SPELL_COST - %d\nCURE_SPELL_COST - %d\nNO_PENALTY_TIME - %d\nADVENTURE_NO_PENALTY_TIME" +
                        " - %d\nSAPPER_NO_PENALTY_TIME - %d\nCOMBO_MINIMUM - %d\nREGEN_MANA_VALUE" +
                        " - %d\nPOISON_VALUE - %d\nBONUS_POINTS_UNFREEZE - %d\nNEXT_WORD_TIME" +
                        " - %d\nDICTIONARY_TEST_LEVELS_SIZE - %d\nDICTIONARY_TEST_TIME_LIMIT - %d\nHSK_BASIC_BONUS_TIME_UNIT" +
                        " - %d\nJACKPOT_BONUS - %d\nMAX_PENALTY - %d\nHIDE_PINYIN_ON_EASY_ON_WORD_DIFFICULTY - %d\n",
                240 * SECONDS, 50, 1, 3, 2, 100, 100, 10, 11, 200, 5, 50, 4, 10, 50, 200, 10, 50, 9, 11, 23, 31, 4, 4,
                NO_PENALTY_TIME * 2, 3, 4 + new Random().nextInt(9), 3 + new Random().nextInt(10), 3, 5000, 100, 300, 30 * SECONDS, 1000, 20, 6);
    }

    private static long calculateTime(Player player) {
        return HSK_BASIC_TIME_LIMIT - (player.getGame().getStopTime() - player.getGame().getStartTime());
    }

    public static int calcJackPot(int level) {
        return JACKPOT_BONUS + (level * new Random().nextInt(4)) + new Random().nextInt(level + 1);
    }

    public static int getPlainBonus(Game game) {
        switch (game.getGameType()) {
            case ADVENTURE:
                return DEFAULT_BONUS_POINTS + Config.calcDictionarySizeBonus(Dictionary.getDictionary().getAllDictionarySize()) + game.getStage().getDifficulty() + Double.valueOf(game.getLevel() / 4).intValue();
            case SAPPER:
                return (int) ((DEFAULT_BONUS_POINTS + calcDictionarySizeBonus(game.getGameWordsList().size()) * game.getStage().getScoreBonusMultiply()) + (game.getLevel() * 3 / 5));
            default:
                Log.e(TAG, "No bonus score implementation for " + game.getGameType().name());
                return 0;
        }
    }

}
