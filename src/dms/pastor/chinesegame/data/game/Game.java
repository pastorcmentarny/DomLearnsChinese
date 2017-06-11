package dms.pastor.chinesegame.data.game;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.common.enums.Stage;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.games.dictionarytest.DictionaryLevelInfo;

import static dms.pastor.chinesegame.Config.DICTIONARY_TEST_LEVELS_SIZE;
import static dms.pastor.chinesegame.Config.SECONDS;
import static dms.pastor.chinesegame.common.enums.GameType.LESSON;
import static dms.pastor.chinesegame.common.enums.GameType.NONE;
import static dms.pastor.chinesegame.common.enums.Stage.STAGE1;
import static dms.pastor.chinesegame.data.Statistic.getStatistic;
import static dms.pastor.chinesegame.data.dictionary.Dictionary.getDictionary;
import static dms.pastor.chinesegame.data.game.Player.getPlayer;
import static dms.pastor.chinesegame.utils.DomUtils.copyXItemsFromOneListToAnother;
import static dms.pastor.chinesegame.utils.DomUtils.shuffle;
import static dms.pastor.chinesegame.utils.UIUtils.displayError;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 28/11/2012
 */
public final class Game {
    private static final String TAG = Config.TAG_PREFIX + "GAME";
    private final Context context;
    private final Statistic statistic;
    private final Dictionary dictionary = getDictionary();
    private final GameType gameType;
    private final Random random = new Random();
    private int level = 1;
    private int levels;
    private Stage stage;
    private long totalTime;
    private long startTime;
    private long stopTime;
    private int correct;
    private int mistake;
    private int skipped;
    private ArrayList<Word> answerWordsForLevels = new ArrayList<>();
    private ArrayList<Word> gameWordsList = new ArrayList<>();

    public Game(Context context, GameType gameType) {
        this.context = context;
        statistic = getStatistic(context);
        this.gameType = gameType;
        clearLevel();
        setGame(gameType);

        level = 1;
    }

    public ArrayList<Word> getGameWordsList() {
        return gameWordsList;
    }

    public void setGameWordList(ArrayList<Word> wordList) {
        this.gameWordsList = wordList;
    }

    private void clearLevel() {
        level = 1;
        levels = 0;
        totalTime = 0L;
        startTime = 0L;
        correct = 0;
        mistake = 0;
        skipped = 0;
    }

    public void addLevel() {
        level++;
        statistic.addLevel();
    }

    public int getLevel() {
        return level;
    }

    public int getLevels() {
        return levels;
    }

    @SuppressWarnings("SameParameterValue")
    public void setLevels(int levels) {
        this.levels = levels;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void timeStart() {
        startTime = new Date().getTime();
    }

    public void timeStop() {
        stopTime = new Date().getTime();
    }

    public long getCurrentTimeInSeconds() {
        return (new Date().getTime() - startTime) / SECONDS;
    }

    public int getTotalTimeInSeconds() {
        return Long.valueOf((stopTime - startTime) / SECONDS).intValue();
    }

    public long getCurrentTime() {
        return (new Date().getTime() - startTime);
    }

    public int getCorrect() {
        return correct;
    }

    private void setCorrect(int correct) {
        this.correct = correct;
    }

    public void addCorrect() {
        setCorrect(correct + 1);
        statistic.addCorrect();

    }

    public void addMistake() {
        mistake++;
        statistic.addWrong();
    }

    public void addSkipped() {
        skipped++;
        statistic.addSkippedAnswer();
    }

    public int getMistake() {
        return mistake;
    }

    public void addToTotalTime(long levelTime) {
        totalTime += levelTime;
    }

    public boolean isLastLevel() {
        return level > levels;
    }

    public long getStopTime() {
        return stopTime;
    }

    public ArrayList<Word> getAnswerWordsForLevels() {
        return answerWordsForLevels;
    }

    public void setAnswerWordsForLevels(ArrayList<Word> answerWordsForLevels) {
        this.answerWordsForLevels = answerWordsForLevels;
    }

    public void createAnswerWordsForLevels(ArrayList<Word> wordsListFromDictionary) {
        ArrayList<Word> randomList = shuffle(wordsListFromDictionary);
        randomList.trimToSize();
        for (int i = levels - 1; i < randomList.size(); i++) {
            randomList.remove(i);
        }
        randomList.trimToSize();
        answerWordsForLevels = randomList;
    }

    public GameType getGameType() {
        return gameType;
    }


    //TODO improve it
    public Word getRandomWordForLevel() {
        Log.d(TAG, "selecting random word");
        if (gameType == null || gameType.equals(NONE)) {
            Log.e(TAG, "No Game type selected");
            if (context != null) {
                displayError(context, "Please restart game.(Unknown game type) ");
            }
        }

        if (gameType != null && gameType.equals(LESSON)) {
            ArrayList<Word> words = getDictionary().getWordsFromCategoryFromDictionary(getPlayer().miniLessons.getCurrentLesson().getGroup());
            return words.get(random.nextInt((words.size() - 1)));
        }

        return getRandomWord();
    }


    private Word getRandomWord() {
        if (gameWordsList == null || gameWordsList.size() <= 0) {
            Log.e(TAG, "gameWordsList is empty!" + getGameState());
        }
        return gameWordsList.get(random.nextInt(gameWordsList.size()));
    }


    private void updateWordListToStage() {
        gameWordsList.clear();
        switch (stage) {
            case STAGE1:
                gameWordsList.addAll(dictionary.getWordsForLevel(1));
                break;
            case STAGE2:
                gameWordsList.addAll(dictionary.getWordsForLevel(2));
                break;
            case STAGE3:
                gameWordsList.addAll(dictionary.getWordsForLevel(3));
                break;
            case STAGE4:
                gameWordsList.addAll(dictionary.getWordsForLevel(4));
                break;
            case STAGE5:
                gameWordsList.addAll(dictionary.getWordsForLevel(5));
                break;
            case STAGE6:
                gameWordsList.addAll(dictionary.getWordsForLevel(6));
                break;
            case STAGE7:
                gameWordsList.addAll(dictionary.getWordsForLevel(7));
                break;
            case STAGE8:
                gameWordsList.addAll(dictionary.getWordsForLevel(8));
                break;
            default:
                Log.e(TAG, "No stage");
        }
    }

    private void setGame(GameType gameType) {
        switch (gameType) {
            case ADVENTURE:
                stage = STAGE1;
                break;
            case SAPPER:
                stage = STAGE1;
                break;
            case HSK0:
                levels = Config.HSK_BASIC_LEVELS_SIZE;
                totalTime = 0;
                ArrayList<Word> gameWords = getDictionary().getWordsFromCategoryFromDictionary(new String[]{"hsk1"});
                setGameWordList(gameWords);
                createAnswerWordsForLevels(gameWords);
                break;
            case LESSON:
                levels = Config.LESSON_LEVELS_SIZE;
                ArrayList<Word> words = getDictionary().getWordsFromCategoryFromDictionary(getPlayer().miniLessons.getCurrentLesson().getGroup());
                setAnswerWordsForLevels(words);
                setGameWordList(words);
                createAnswerWordsForLevels(words);
                break;
            case DICTIONARY_TEST:
                statistic.addDictionaryTestGame();
                totalTime = 0;
                levels = DICTIONARY_TEST_LEVELS_SIZE;
                generateWordsForDictionaryTest();
            default:
                Log.w(TAG, "Unknown game type selection during set game (" + gameType.name());
                break;
        }
    }

    private void generateWordsForDictionaryTest() {
        ArrayList<Word> wordList;
        for (int i = 1; i <= Stage.values().length; i++) {
            wordList = dictionary.getWordsForLevel(i);
            shuffle(wordList);
            copyXItemsFromOneListToAnother(DictionaryLevelInfo.getStageSizeForStage(i), wordList, answerWordsForLevels);
        }
        shuffle(answerWordsForLevels);
    }

    public void nextTurn() {
        setStage(level);
    }

    public Stage getStage() {
        return stage;
    }

    private void setStage(int level) {
        Stage tmp = stage;
        stage = Stage.getStageForLevel(level);
        if (stage != null && tmp != null && !tmp.equals(stage)) {
            updateWordListToStage();
        }
    }

    private String getGameState() {
        return "Game{" +
                ", level=" + level +
                ", levels=" + levels +
                ", stage=" + stage +
                ", totalTime=" + totalTime +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", correct=" + correct +
                ", mistake=" + mistake +
                ", mistake=" + skipped +
                ", gameType=" + gameType.name() +
                ", answerWordsForLevels=" + (answerWordsForLevels != null ? answerWordsForLevels.size() : "NULL") +
                ", gameWordsList=" + (gameWordsList != null ? gameWordsList.size() : "NULL") +
                '}';
    }


    public int getSkipped() {
        return skipped;
    }
}
