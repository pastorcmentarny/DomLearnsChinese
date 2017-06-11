package dms.pastor.chinesegame.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.dictionary.WordMistake;
import dms.pastor.chinesegame.utils.CryptoUtils;
import dms.pastor.chinesegame.utils.FileUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 *
 * Created 02/11/2012
 *
 * if you add new value,then, add to reset() change set to add
 */
public final class Statistic {
    private static final String TAG = "STATISTIC";
    private static final String GAMES_KEY = "games";
    private static final String ADVENTURE_GAMES_KEY = "adventureGames";
    private static final String SAPPER_GAMES_KEY = "sapperGames";
    private static final String DICTIONARY_GAMES_KEY = "dictionaryTestGames";
    private static final String TOTAL_TIME_KEY = "totalTime";
    private static final String CORRECTS_KEY = "corrects";
    private static final String WRONGS_KEY = "wrongs";
    private static final String SKIPPED_KEY = "skipped";
    private static final String MAX_COMBO_KEY = "maxCombo";
    private static final String MAX_COMBO_POINTS_KEY = " maxComboPoints";
    private static final String MAX_SCORE_KEY = "maxScore";
    private static final String TOTAL_LEVELS_KEY = "totalLevels";
    private static final String HIGHEST_LEVEL_SAPPER_KEY = "highestLevelSapper";
    private static final String HIGHEST_LEVEL_ADVENTURES_KEY = "highestLevelAdventures";
    private static final String TOTAL_TIME_SAPPER_KEY = "totalTimeSapper";
    private static final String TOTAL_TIME_ADVENTURE_KEY = "totalTimeAdventure";
    private static final String SHOW_PINYIN_USED_KEY = "showPinyinUsed";
    private static final String HEAL_USED_KEY = "healUsed";
    private static final String REMOVE_WRONG_ANSWER_USED_KEY = "removeWrongAnswerUsed";
    private static final String CURE_USED_KEY = "cureUsed";
    private static final String HALF_HALF_HAPPEN_KEY = "halfHalfHappen";
    private static final String POISON_HAPPEN_KEY = "poisonHappen";
    private static final String TRIPLE_KEY = "triple";
    private static final String JACKPOT_KEY = "jackpot";
    private static final String DRAIN_MANA_KEY = "drainMana";
    private static final String MINOR_POINTS_KEY = "minorPoints";
    private static final String REGENERATION_KEY = "regeneration";
    private static final String SHIT_HAPPENS_KEY = "shitHappens";
    private static final String SWAP_HP_MP_KEY = "swapHpMp";
    private static final String FROZEN_KEY = "frozen";
    private static final String BLIND_KEY = "blind";
    private static final String SHOW_PINYIN_KEY = "showPinyin";
    private static final String PAIN_KILLER_KEY = "painKiller";
    private static final String SHOW_PINYIN_HIDE_CHARACTER_KEY = "showPinyinHideCharacter";
    private static final String DOUBLE_HP_KEY = "doubleHP";
    private static final String DOUBLE_MP_KEY = "doubleMP";
    private static final String HALF_HP_KEY = "halfHP";
    private static final String HALF_MP_KEY = "halfMP";
    private static final String SAPPER_EASY_KEY = "sapperEasy";
    private static final String SAPPER_HARD_KEY = "sapperHard";
    private static Statistic statistic;
    private final SharedPreferences stats;
    private final SharedPreferences settings;
    private final StringBuilder line = new StringBuilder("");
    //Game
    private int games = 0;
    private int adventureGames = 0;
    private int sapperGames = 0;
    private int dictionaryTestGames = 0;
    private long totalTime = 0;
    //level
    private int corrects = 0;
    private int wrongs = 0;
    private int skipped = 0;
    private int maxCombo = 0;
    private int maxComboPoints = 0;
    private int maxScore = 0;
    private int totalLevels = 0;
    private int highestLevelSapper = 0;
    private int highestLevelAdventures = 0;
    private long totalTimeSapper = 0;
    private long totalTimeAdventure = 0;
    //spells
    private int showPinyinUsed = 0;
    private int healUsed = 0;
    private int removeWrongAnswerUsed = 0;
    private int cureUsed = 0;
    //event
    private int halfHalfHappen = 0;
    private int poisonHappen = 0;
    private int triple = 0;
    private int jackpot = 0;
    private int drainMana = 0;
    private int minorPoints = 0;
    private int regeneration = 0;
    private int shitHappens = 0;
    private int swapHpMp = 0;
    private int frozen = 0;
    private int blind = 0;
    private int showPinyin = 0;
    private int painKiller = 0;
    private int showPinyinHideCharacter = 0;
    private int doubleHP = 0;
    private int doubleMP = 0;
    private int halfHP = 0;
    private int halfMP = 0;
    //game difficulties
    private int sapperEasy = 0;
    private int sapperHard = 0;
    private String valueTmp;
    private String typeTmp;

    private Statistic(Context context) {
        stats = context.getSharedPreferences("stats", Context.MODE_PRIVATE);
        settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        load();

    }

    /**
     * get new instance of high score,if was not created.
     *
     * @return HighScore if was not instanced.
     */
    public static synchronized Statistic getStatistic(Context context) {

        if (statistic == null) {
            statistic = new Statistic(context);
        }
        return statistic;
    }

    public int getSkipped() {
        return skipped;
    }

    public void addSkipped() {
        skipped++;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Object clone() throws CloneNotSupportedException {
        Log.e(TAG, "Clone not supported exception!");
        throw new CloneNotSupportedException();
    }

    public boolean reset() {
        games = 0;
        adventureGames = 0;
        sapperGames = 0;
        dictionaryTestGames = 0;
        totalTime = 0;

        //level
        corrects = 0;
        wrongs = 0;
        skipped = 0;
        maxCombo = 0;
        maxComboPoints = 0;
        maxScore = 0;
        totalLevels = 0;
        highestLevelSapper = 0;
        highestLevelAdventures = 0;
        totalTimeSapper = 0;
        totalTimeAdventure = 0;


        //spells
        showPinyinUsed = 0;
        healUsed = 0;
        removeWrongAnswerUsed = 0;
        cureUsed = 0;


        //event
        halfHalfHappen = 0;
        poisonHappen = 0;
        triple = 0;
        jackpot = 0;
        drainMana = 0;
        minorPoints = 0;
        regeneration = 0;
        shitHappens = 0;
        swapHpMp = 0;
        frozen = 0;
        blind = 0;
        showPinyin = 0;
        painKiller = 0;
        showPinyinHideCharacter = 0;
        doubleHP = 0;
        doubleMP = 0;
        halfHP = 0;
        halfMP = 0;

        //game difficulties
        sapperEasy = 0;
        sapperHard = 0;
        return save();
    }


    public boolean save() {
        SharedPreferences.Editor preferencesEditor = stats.edit();
        preferencesEditor.putInt(GAMES_KEY, games);
        preferencesEditor.putInt(ADVENTURE_GAMES_KEY, adventureGames);
        preferencesEditor.putInt(SAPPER_GAMES_KEY, sapperGames);
        preferencesEditor.putInt(DICTIONARY_GAMES_KEY, dictionaryTestGames);
        preferencesEditor.putLong(TOTAL_TIME_KEY, totalTime);

        //level
        preferencesEditor.putInt(CORRECTS_KEY, corrects);
        preferencesEditor.putInt(WRONGS_KEY, wrongs);
        preferencesEditor.putInt(SKIPPED_KEY, skipped);
        preferencesEditor.putInt(MAX_COMBO_KEY, maxCombo);
        preferencesEditor.putInt(MAX_COMBO_POINTS_KEY, maxComboPoints);
        preferencesEditor.putInt(MAX_SCORE_KEY, maxScore);
        preferencesEditor.putInt(TOTAL_LEVELS_KEY, totalLevels);
        preferencesEditor.putInt(HIGHEST_LEVEL_SAPPER_KEY, highestLevelSapper);
        preferencesEditor.putInt(HIGHEST_LEVEL_ADVENTURES_KEY, highestLevelAdventures);
        preferencesEditor.putLong(TOTAL_TIME_SAPPER_KEY, totalTimeSapper);
        preferencesEditor.putLong(TOTAL_TIME_ADVENTURE_KEY, totalTimeAdventure);

        //spells
        preferencesEditor.putInt(SHOW_PINYIN_USED_KEY, showPinyinUsed);
        preferencesEditor.putInt(HEAL_USED_KEY, healUsed);
        preferencesEditor.putInt(REMOVE_WRONG_ANSWER_USED_KEY, removeWrongAnswerUsed);
        preferencesEditor.putInt(CURE_USED_KEY, cureUsed);


        //event
        preferencesEditor.putInt(HALF_HALF_HAPPEN_KEY, halfHalfHappen);
        preferencesEditor.putInt(POISON_HAPPEN_KEY, poisonHappen);
        preferencesEditor.putInt(TRIPLE_KEY, triple);
        preferencesEditor.putInt(JACKPOT_KEY, jackpot);
        preferencesEditor.putInt(DRAIN_MANA_KEY, drainMana);
        preferencesEditor.putInt(MINOR_POINTS_KEY, minorPoints);
        preferencesEditor.putInt(REGENERATION_KEY, regeneration);
        preferencesEditor.putInt(SHIT_HAPPENS_KEY, shitHappens);
        preferencesEditor.putInt(SWAP_HP_MP_KEY, swapHpMp);
        preferencesEditor.putInt(FROZEN_KEY, frozen);
        preferencesEditor.putInt(BLIND_KEY, blind);
        preferencesEditor.putInt(SHOW_PINYIN_KEY, showPinyin);
        preferencesEditor.putInt(PAIN_KILLER_KEY, painKiller);
        preferencesEditor.putInt(SHOW_PINYIN_HIDE_CHARACTER_KEY, showPinyinHideCharacter);
        preferencesEditor.putInt(DOUBLE_HP_KEY, doubleHP);
        preferencesEditor.putInt(DOUBLE_MP_KEY, doubleMP);
        preferencesEditor.putInt(HALF_HP_KEY, halfHP);
        preferencesEditor.putInt(HALF_MP_KEY, halfMP);

        //game difficulties
        preferencesEditor.putInt(SAPPER_EASY_KEY, sapperEasy);
        preferencesEditor.putInt(SAPPER_HARD_KEY, sapperHard);


        boolean r = preferencesEditor.commit();
        if (r) {
            Log.i(TAG, "Statistic saved.");
        } else {
            Log.w(TAG, "Statistic not saved :(");
        }
        return r;
    }

    private void load() {
        games = stats.getInt(GAMES_KEY, games);
        adventureGames = stats.getInt(ADVENTURE_GAMES_KEY, adventureGames);
        sapperGames = stats.getInt(SAPPER_GAMES_KEY, sapperGames);
        dictionaryTestGames = stats.getInt(DICTIONARY_GAMES_KEY, dictionaryTestGames);
        totalTime = stats.getLong(TOTAL_TIME_KEY, totalTime);

        //level
        corrects = stats.getInt(CORRECTS_KEY, corrects);
        wrongs = stats.getInt(WRONGS_KEY, wrongs);
        skipped = stats.getInt(SKIPPED_KEY, skipped);
        maxCombo = stats.getInt(MAX_COMBO_KEY, maxCombo);
        maxComboPoints = stats.getInt(MAX_COMBO_POINTS_KEY, maxComboPoints);
        maxScore = stats.getInt(MAX_SCORE_KEY, maxScore);
        totalLevels = stats.getInt(TOTAL_LEVELS_KEY, totalLevels);
        highestLevelSapper = stats.getInt(HIGHEST_LEVEL_SAPPER_KEY, highestLevelSapper);
        highestLevelAdventures = stats.getInt(HIGHEST_LEVEL_ADVENTURES_KEY, highestLevelAdventures);
        totalTimeSapper = stats.getLong(TOTAL_TIME_SAPPER_KEY, totalTimeSapper);
        totalTimeAdventure = stats.getLong(TOTAL_TIME_ADVENTURE_KEY, totalTimeAdventure);

        //spells
        showPinyinUsed = stats.getInt(SHOW_PINYIN_USED_KEY, showPinyinUsed);
        healUsed = stats.getInt(HEAL_USED_KEY, healUsed);
        removeWrongAnswerUsed = stats.getInt(REMOVE_WRONG_ANSWER_USED_KEY, removeWrongAnswerUsed);
        cureUsed = stats.getInt(CURE_USED_KEY, cureUsed);


        //event
        halfHalfHappen = stats.getInt(HALF_HALF_HAPPEN_KEY, halfHalfHappen);
        poisonHappen = stats.getInt(POISON_HAPPEN_KEY, poisonHappen);
        triple = stats.getInt(TRIPLE_KEY, triple);
        jackpot = stats.getInt(JACKPOT_KEY, jackpot);
        drainMana = stats.getInt(DRAIN_MANA_KEY, drainMana);
        minorPoints = stats.getInt(MINOR_POINTS_KEY, minorPoints);
        regeneration = stats.getInt(REGENERATION_KEY, regeneration);
        shitHappens = stats.getInt(SHIT_HAPPENS_KEY, shitHappens);
        swapHpMp = stats.getInt(SWAP_HP_MP_KEY, swapHpMp);
        frozen = stats.getInt(FROZEN_KEY, frozen);
        blind = stats.getInt(BLIND_KEY, blind);
        showPinyin = stats.getInt(SHOW_PINYIN_KEY, showPinyin);
        painKiller = stats.getInt(PAIN_KILLER_KEY, painKiller);
        showPinyinHideCharacter = stats.getInt(SHOW_PINYIN_HIDE_CHARACTER_KEY, showPinyinHideCharacter);
        doubleHP = stats.getInt(DOUBLE_HP_KEY, doubleHP);
        doubleMP = stats.getInt(DOUBLE_MP_KEY, doubleMP);
        halfHP = stats.getInt(HALF_HP_KEY, halfHP);
        halfMP = stats.getInt(HALF_MP_KEY, halfMP);

        //game difficulties
        sapperEasy = stats.getInt(SAPPER_EASY_KEY, sapperEasy);
        sapperHard = stats.getInt(SAPPER_HARD_KEY, sapperHard);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String getStatsAsText() {
        StringBuilder stats2backup = new StringBuilder("");
        stats2backup.append(add(GAMES_KEY, stats.getInt(GAMES_KEY, games)));
        stats2backup.append(add(ADVENTURE_GAMES_KEY, stats.getInt(ADVENTURE_GAMES_KEY, adventureGames)));
        stats2backup.append(add(SAPPER_GAMES_KEY, stats.getInt(SAPPER_GAMES_KEY, sapperGames)));
        stats2backup.append(add(DICTIONARY_GAMES_KEY, stats.getInt(DICTIONARY_GAMES_KEY, dictionaryTestGames)));
        stats2backup.append(add(TOTAL_TIME_KEY, stats.getLong(TOTAL_TIME_KEY, totalTime)));

        //level
        stats2backup.append(add(CORRECTS_KEY, stats.getInt(CORRECTS_KEY, corrects)));
        stats2backup.append(add(WRONGS_KEY, stats.getInt(WRONGS_KEY, wrongs)));
        stats2backup.append(add(SKIPPED_KEY, stats.getInt(SKIPPED_KEY, skipped)));
        stats2backup.append(add(MAX_COMBO_KEY, stats.getInt(MAX_COMBO_KEY, maxCombo)));
        stats2backup.append(add(MAX_COMBO_POINTS_KEY, stats.getInt(MAX_COMBO_POINTS_KEY, maxComboPoints)));
        stats2backup.append(add(MAX_SCORE_KEY, stats.getInt(MAX_SCORE_KEY, maxScore)));
        stats2backup.append(add(TOTAL_LEVELS_KEY, stats.getInt(TOTAL_LEVELS_KEY, totalLevels)));
        stats2backup.append(add(HIGHEST_LEVEL_SAPPER_KEY, stats.getInt(HIGHEST_LEVEL_SAPPER_KEY, highestLevelSapper)));
        stats2backup.append(add(HIGHEST_LEVEL_ADVENTURES_KEY, stats.getInt(HIGHEST_LEVEL_ADVENTURES_KEY, highestLevelAdventures)));
        stats2backup.append(add(TOTAL_TIME_SAPPER_KEY, stats.getLong(TOTAL_TIME_SAPPER_KEY, totalTimeSapper)));
        stats2backup.append(add(TOTAL_TIME_ADVENTURE_KEY, stats.getLong(TOTAL_TIME_ADVENTURE_KEY, totalTimeAdventure)));


        //spells
        stats2backup.append(add(SHOW_PINYIN_USED_KEY, stats.getInt(SHOW_PINYIN_USED_KEY, showPinyinUsed)));
        stats2backup.append(add(HEAL_USED_KEY, stats.getInt(HEAL_USED_KEY, healUsed)));
        stats2backup.append(add(REMOVE_WRONG_ANSWER_USED_KEY, stats.getInt(REMOVE_WRONG_ANSWER_USED_KEY, removeWrongAnswerUsed)));
        stats2backup.append(add(CURE_USED_KEY, stats.getInt(CURE_USED_KEY, cureUsed)));

        //event
        stats2backup.append(add(HALF_HALF_HAPPEN_KEY, stats.getInt(HALF_HALF_HAPPEN_KEY, halfHalfHappen)));
        stats2backup.append(add(POISON_HAPPEN_KEY, stats.getInt(POISON_HAPPEN_KEY, poisonHappen)));
        stats2backup.append(add(TRIPLE_KEY, stats.getInt(TRIPLE_KEY, triple)));
        stats2backup.append(add(JACKPOT_KEY, stats.getInt(JACKPOT_KEY, jackpot)));
        stats2backup.append(add(DRAIN_MANA_KEY, stats.getInt(DRAIN_MANA_KEY, drainMana)));
        stats2backup.append(add(MINOR_POINTS_KEY, stats.getInt(MINOR_POINTS_KEY, minorPoints)));
        stats2backup.append(add(REGENERATION_KEY, stats.getInt(REGENERATION_KEY, regeneration)));
        stats2backup.append(add(SHIT_HAPPENS_KEY, stats.getInt(SHIT_HAPPENS_KEY, shitHappens)));
        stats2backup.append(add(SWAP_HP_MP_KEY, stats.getInt(SWAP_HP_MP_KEY, swapHpMp)));
        stats2backup.append(add(FROZEN_KEY, stats.getInt(FROZEN_KEY, frozen)));
        stats2backup.append(add(BLIND_KEY, stats.getInt(BLIND_KEY, blind)));
        stats2backup.append(add(SHOW_PINYIN_KEY, stats.getInt(SHOW_PINYIN_KEY, showPinyin)));
        stats2backup.append(add(PAIN_KILLER_KEY, stats.getInt(PAIN_KILLER_KEY, painKiller)));
        stats2backup.append(add(SHOW_PINYIN_HIDE_CHARACTER_KEY, stats.getInt(SHOW_PINYIN_HIDE_CHARACTER_KEY, showPinyinHideCharacter)));
        stats2backup.append(add(DOUBLE_HP_KEY, stats.getInt(DOUBLE_HP_KEY, doubleHP)));
        stats2backup.append(add(DOUBLE_MP_KEY, stats.getInt(DOUBLE_MP_KEY, doubleMP)));
        stats2backup.append(add(HALF_HP_KEY, stats.getInt(HALF_HP_KEY, halfHP)));
        stats2backup.append(add(HALF_MP_KEY, stats.getInt(HALF_MP_KEY, halfMP)));

        //game difficulties
        stats2backup.append(add(SAPPER_EASY_KEY, stats.getInt(SAPPER_EASY_KEY, sapperEasy)));
        stats2backup.append(add(SAPPER_HARD_KEY, stats.getInt(SAPPER_HARD_KEY, sapperHard)));
        return stats2backup.toString();

    }

    private String add(String title, Object value) {

        if (value instanceof Integer) {
            valueTmp = String.valueOf(value);
            typeTmp = "int";
        } else if (value instanceof Long) {
            valueTmp = String.valueOf(value);
            typeTmp = "long";
        }
        line.setLength(0);
        line.append(Config.SPR).append(title).append(Config.SPR).append(valueTmp).append(Config.SPR).append(typeTmp).append(Config.SPR).append("\n");
        return line.toString();
    }

    private boolean get(String entry) {
        String[] anEntry = entry.split(Config.SPR);
        SharedPreferences.Editor preferencesEditor = stats.edit();
        if (anEntry.length > 3) {
            try {
                if (anEntry[3].equalsIgnoreCase("int")) {
                    preferencesEditor.putInt(anEntry[1], Integer.parseInt(anEntry[2]));
                } else if (anEntry[3].equalsIgnoreCase("long")) {
                    preferencesEditor.putLong(anEntry[1], Long.parseLong(anEntry[2]));
                } else {
                    Log.e(TAG, "Unsupported entry type!" + anEntry[3]);
                    return false;
                }
                Log.i("Entry:", entry + "saved");
            } catch (NumberFormatException nfe) {
                Log.e(TAG, "Data must be corrupted entry as this value is wrong type: " + anEntry[2]);
                return false;
            }
        }


        boolean r = preferencesEditor.commit();
        if (r) {
            Log.i(TAG, "Statistic saved.");
        } else {
            Log.w(TAG, "Statistic not saved :(");
        }
        return r;
    }


    public boolean backup() throws Exception {
        Log.i(TAG, "Start backup of statistic..");
        String data = getStatsAsText();
        Log.e(TAG, "Encrypting statistic..");
        String encryptedData = CryptoUtils.encrypt(Config.getKeyAsBytes(), data.getBytes());
        Log.e(TAG, "Saving statistics to backup folder");
        File f = Config.STATISTIC_FILE;
        Log.i("Backup file:", f.getAbsolutePath());
        return FileUtils.saveTextToFile(encryptedData, f);
    }


    //TODO redesign as this method ass current method could win WTF is this piece of crap awards
    public boolean restore() {
        File statsFile = Config.STATISTIC_FILE;
        Log.i(TAG, "Start restoring statistic..");
        byte[] loaded = FileUtils.loadTextFromFile(statsFile).getBytes();
        Log.i(TAG, "Decrypting...");
        try {
            byte[] decryptedData = CryptoUtils.decrypt(Config.getKeyAsBytes(), loaded);
            String[] xx = new String(decryptedData).split("\n");
            for (String x : xx) {
                if (!get(x)) {
                    return false;
                }
            }
            load();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "lol" + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "stats=" + stats +
                ", games=" + games +
                ", adventureGames=" + adventureGames +
                ", sapperGames=" + sapperGames +
                ", dictionaryTestGames=" + dictionaryTestGames +
                ", totalTime=" + totalTime +
                ", corrects=" + corrects +
                ", wrongs=" + wrongs +
                ", skipped=" + skipped +
                ", maxCombo=" + maxCombo +
                ", maxComboPoints=" + maxComboPoints +
                ", maxScore=" + maxScore +
                ", totalLevels=" + totalLevels +
                ", highestLevelSapper=" + highestLevelSapper +
                ", highestLevelAdventures=" + highestLevelAdventures +
                ", totalTimeSapper=" + totalTimeSapper +
                ", totalTimeAdventure=" + totalTimeAdventure +
                ", showPinyinUsed=" + showPinyinUsed +
                ", healUsed=" + healUsed +
                ", removeWrongAnswerUsed=" + removeWrongAnswerUsed +
                ", cureUsed=" + cureUsed +
                ", halfHalfHappen=" + halfHalfHappen +
                ", poisonHappen=" + poisonHappen +
                ", triple=" + triple +
                ", jackpot=" + jackpot +
                ", drainMana=" + drainMana +
                ", minorPoints=" + minorPoints +
                ", regeneration=" + regeneration +
                ", shitHappens=" + shitHappens +
                ", swapHpMp=" + swapHpMp +
                ", frozen=" + frozen +
                ", blind=" + blind +
                ", showPinyin=" + showPinyin +
                ", painKiller=" + painKiller +
                ", sapperEasy=" + sapperEasy +
                ", sapperHard=" + sapperHard +
                '}';
    }

    public int getGames() {
        return games;
    }

    public void addGame() {
        games++;
    }

    public int getAdventureGames() {
        return adventureGames;
    }

    public void addAdventureGame() {
        games++;
        adventureGames++;
        save();
    }

    public int getSapperGames() {
        return sapperGames;
    }

    public void addSapperGame() {
        games++;
        sapperGames++;
        if (settings.getBoolean("sapper_pinyin", false)) {
            sapperEasy++;
        } else {
            sapperHard++;
        }
        save();
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getCorrects() {
        return corrects;
    }

    public void addCorrect() {
        corrects++;
    }

    public void addCorrects(int corrects) {
        this.corrects += corrects;
    }

    public int getWrongs() {
        return wrongs;
    }

    public void addWrong() {
        wrongs++;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    private void setMaxCombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public void addToMaxCombo(int newCombo) {
        if (newCombo > maxCombo) {
            setMaxCombo(newCombo);
        }
    }

    public int getMaxComboPoints() {
        return maxComboPoints;
    }

    private void setMaxComboPoints(int maxComboPoints) {
        this.maxComboPoints = maxComboPoints;
    }


    public void addToMaxComboPoints(int newComboPoints) {
        if (newComboPoints > maxComboPoints) {
            setMaxComboPoints(newComboPoints);
        }
    }

    public int getMaxScore() {
        return maxScore;
    }

    private void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void addToMaxScore(int newScore) {
        if (newScore > maxScore) {
            setMaxScore(newScore);
        } else {
            Log.d(TAG, "new score is smaller than highest score");
        }
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void addLevel() {
        totalLevels++;
    }

    public void addTotalLevels(int levels) {
        totalLevels += levels;
    }

    public int getHighestLevelSapper() {
        return highestLevelSapper;
    }

    private void setHighestLevelSapper(int highestLevelSapper) {
        this.highestLevelSapper = highestLevelSapper;
    }


    public void addToHighestLevelSapper(int newLevel) {
        if (newLevel > highestLevelSapper) {
            setHighestLevelSapper(newLevel);
        }
    }

    public int getHighestLevelAdventures() {
        return highestLevelAdventures;
    }

    private void setHighestLevelAdventures(int highestLevelAdventures) {
        this.highestLevelAdventures = highestLevelAdventures;
    }

    public void addToHighestLevelAdventures(int newLevel) {
        if (newLevel > highestLevelAdventures) {
            setHighestLevelAdventures(newLevel);
        }
    }

    public long getTotalTimeSapper() {
        return totalTimeSapper;
    }

    public void addToTotalTimeSapper(long newTotalTime) {
        totalTimeSapper += newTotalTime;
        totalTime += newTotalTime;
    }

    public long getTotalTimeAdventure() {
        return totalTimeAdventure;
    }

    public void addToTotalTimeAdventure(long newTotalTime) {
        totalTimeAdventure += newTotalTime;
        totalTime += newTotalTime;
    }


    public int getShowPinyinUsed() {
        return showPinyinUsed;
    }

    public void addPinyinUsed() {
        showPinyinUsed++;
    }

    public int getHealUsed() {
        return healUsed;
    }

    public void addHealUsed() {
        healUsed++;
    }

    public int getRemoveWrongAnswerUsed() {
        return removeWrongAnswerUsed;
    }

    public void addRemoveWrongAnswerUsed() {
        removeWrongAnswerUsed++;
    }


    public int getCureUsed() {
        return cureUsed;
    }


    public void addCureUsed() {
        cureUsed++;
    }

    public int getHalfHalfHappen() {
        return halfHalfHappen;
    }

    public void addHalfHalfHappen() {
        halfHalfHappen++;
    }

    public int getPoisonHappen() {
        return poisonHappen;
    }

    public void addPoisonHappen() {
        poisonHappen++;
    }

    public int getTriple() {
        return triple;
    }

    public void addTripe() {
        triple++;
    }

    public int getJackpot() {
        return jackpot;
    }

    public void addJackpot() {
        jackpot++;
    }

    public int getDrainMana() {
        return drainMana;
    }

    public void addDrainMana() {
        drainMana++;
    }

    public int getMinorPoints() {
        return minorPoints;
    }

    public void addMinorPoints() {
        minorPoints++;
    }

    public int getRegeneration() {
        return regeneration;
    }

    public void addRegeneration() {
        regeneration++;
    }

    public int getShitHappens() {
        return shitHappens;
    }

    public void addShitHappens() {
        shitHappens++;
    }

    public int getSwapHpMp() {
        return swapHpMp;
    }


    public void addSwapHpMp() {
        swapHpMp++;
    }

    public int getFrozen() {
        return frozen;
    }


    public void addFrozen() {
        frozen++;
    }

    public int getBlind() {
        return blind;
    }


    public void addBlind() {
        blind++;
    }

    public int getShowPinyin() {
        return showPinyin;
    }


    public void addShowPinyin() {
        showPinyin++;
    }

    public void addPainKiller() {
        painKiller++;
    }

    public int getPainKiller() {
        return painKiller;
    }


    public int getSapperDiffEasy() {
        return sapperEasy;
    }

    public int getSapperDiffHard() {
        return sapperHard;
    }

    public void addShowPinyinHideCharacter() {
        showPinyinHideCharacter++;
    }

    public int getShowPinyinHideCharacter() {
        return showPinyinHideCharacter;
    }

    public void addWordToWordMistake(int id) {
        SharedPreferences.Editor preferencesEditor = stats.edit();
        preferencesEditor.putInt(Config.WORD_PREFIX_FOR_STATS + id, stats.getInt(Config.WORD_PREFIX_FOR_STATS + id, 0) + 1);
        preferencesEditor.apply();
    }


    private WordMistake getWordMistakeFromId(int id) {
        Word word = Dictionary.getDictionary().getWordFromDictionary(id);
        if (word != null) {
            int counter = stats.getInt(Config.WORD_PREFIX_FOR_STATS + id, 0);
            return new WordMistake(word, counter);
        } else {
            return null;//TODO replace with Word Not Found Exception //if not found remove from list
        }
    }

    public ArrayList<WordMistake> getSortedWordMistakes() {
        ArrayList<WordMistake> wordMistakes = new ArrayList<>();
        ArrayList<Word> words = Dictionary.getDictionary().getWordsList();
        for (Word word : words) {
            WordMistake wordMistake = getWordMistakeFromId(word.getId());
            if (wordMistake != null && wordMistake.getCounter() > 0) {
                wordMistakes.add(wordMistake);
            }
        }

        Collections.sort(wordMistakes, new Comparator<WordMistake>() {
            @Override
            public int compare(WordMistake first, WordMistake second) {
                return Integer.valueOf(first.getCounter()).compareTo(second.getCounter());
            }
        });
        Collections.reverse(wordMistakes);
        return wordMistakes;
    }


    public void addDoubleHP() {
        doubleHP++;
    }

    public void addHalfHP() {
        halfHP++;
    }

    public void addDoubleMP() {
        doubleMP++;
    }

    public void addHalfMP() {
        halfMP++;
    }

    public void addSkippedAnswer() {
        skipped++;
    }

    public void addDictionaryTestGame() {
        games++;
        dictionaryTestGames++;
        save();
    }
}
