package dms.pastor.chinesegame.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.common.exceptions.NullListException;
import dms.pastor.chinesegame.data.CultureInfoItem;
import dms.pastor.chinesegame.data.GrammarTipItem;
import dms.pastor.chinesegame.data.LinkItem;
import dms.pastor.chinesegame.data.ProverbsItem;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Question;
import dms.pastor.chinesegame.data.dictionary.Sentence;
import dms.pastor.chinesegame.data.learning.chats.Chat;
import dms.pastor.chinesegame.data.learning.lessons.Lesson;
import dms.pastor.chinesegame.data.learning.patterns.Pattern;
import dms.pastor.chinesegame.utils.Result;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static dms.pastor.chinesegame.data.dictionary.Dictionary.recreateDictionary;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 02/09/2013
 */
public final class DatabaseManager {
    private static final String TAG = Config.TAG_PREFIX + "DB Manager";
    private static final int DEFAULT_DB_VERSION = -1;

    private static DatabaseManager dbManager = null;
    private static DatabaseService dbService = null;

    private static Dictionary dictionary = null;
    private final Context context;

    private DatabaseManager(Context context) {
        this.context = context;
        activateDatabaseService();
        setDictionary(Dictionary.getDictionary());
        getDictionary().readDictionaryFromFile(context, R.raw.dictionary, null);
    }

    public static synchronized DatabaseManager getDbManager(Context context) {
        if (dbManager == null) {
            dbManager = new DatabaseManager(context);
        }
        return dbManager;
    }

    public static boolean isUpdateNeeded(Context context) {
        try {
            int appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            return appVersion > getCurrentVersion(context);

        } catch (NameNotFoundException e) {
            Log.w(TAG, "NameNotFoundException  thrown while checking is update is needed .Database will be refreshed.");
            return true;
        }
    }

    public static boolean isNotOK() {
        return !isOK();
    }

    public static Result setUpdated(Context context) {
        Result result = Result.failed();
        SharedPreferences settings = getDefaultSharedPreferences(context);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        try {
            int newVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            preferencesEditor.putInt("dbVersion", newVersion);
        } catch (NameNotFoundException e) {
            result.setMessage("Unable to update database due: " + e.getMessage() + " .Go to settings and press Fix problems.If problem still exists .Reinstall app and inform me about this problems.\nSorry for troubles!");
        }
        boolean r = preferencesEditor.commit();
        result.setSuccess(r);
        if (result.isSuccess()) {
            result.setMessage("Database updated");
        } else {
            result.setMessage("Unable to update database.Go to settings and press Fix problems.If problem still exists .Reinstall app and inform me about this problems.\nSorry for troubles!");
        }
        return result;
    }

    public static DatabaseService getDbService() {
        return dbService;
    }

    private static void setDbService(DatabaseService dbService) {
        DatabaseManager.dbService = dbService;
    }

    private static boolean isOK() {
        return dbManager != null && getDbService() != null && getDbService().isOK();
        //TODO add dictionary != null,where dictionary will be update
    }

    public static String[] getQuestionAsWordList(List<Question> questionList) {
        ArrayList<String> wordList = new ArrayList<>();
        for (Question q : questionList) {
            wordList.add(q.getEnglish());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public static String[] getSentencesAsWordList(List<Sentence> sentenceList) {
        ArrayList<String> wordList = new ArrayList<>();
        for (Sentence s : sentenceList) {
            wordList.add(s.getEnglish());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public static String[] getPatternsAsWordList(List<Pattern> patternList) {
        ArrayList<String> wordList = new ArrayList<>();
        for (Pattern s : patternList) {
            wordList.add(s.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    @SuppressWarnings("CallToSystemGC")
    public static Result reloadAll(Context context) {

        setDictionary(null);

        setDbService(null);
        dbManager = null;

        dbManager = DatabaseManager.getDbManager(context);

        getDbService().fixDB();

        setDictionary(recreateDictionary());
        dictionary.readDictionaryFromFile(context, R.raw.dictionary, null);
        return test(context);
    }

    @SuppressWarnings("ConstantConditions")
    private static Result test(Context context) {
        StringBuilder sb = new StringBuilder("");
        if (dbManager != null) {
            sb.append("Database Manager works.\n");
        } else {
            sb.append("Database Manager doesn't work");
            dbManager = new DatabaseManager(context);
            if (dbManager == null) {
                sb.append("Problem!\nDatabase Manager cannot be created!\n Please reinstall app.\n");
                return new Result(false, sb.toString());
            } else {
                sb.append("Problem with Database Manager was fixed!\n");
            }

        }
        if (getDbService() != null) {
            sb.append("Database Service works.\n");
        } else {
            sb.append("Database Service doesn't work");
            setDbService(new DatabaseService(context));
            if (getDbService() == null) {
                sb.append("Problem!\nDatabase Service cannot be created!\n Please reinstall app.");
                return new Result(false, sb.toString());
            } else {
                sb.append("Problem with Database Service was fixed!\n");
            }
        }

        if (dictionary != null) {
            sb.append("Dictionary exists.\n");
        } else {
            sb.append("Dictionary doesn't work");
            setDictionary(recreateDictionary());
            if (dictionary == null) {
                sb.append("Problem!\nDictionary cannot be created!\n Please reinstall app.");
                return new Result(false, sb.toString());
            } else {
                sb.append("Problem with Dictionary was fixed!\n");
            }
        }
        return new Result(true, sb.toString());
    }

    private static int getCurrentVersion(Context context) {
        SharedPreferences preferences = getDefaultSharedPreferences(context);
        return preferences.getInt("dbVersion", DEFAULT_DB_VERSION);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Only one instance is allowed.");
    }

    //TODO need be improved
    private synchronized void activateDatabaseService() {
        Log.i(TAG, "Connecting to Database");
        setDbService(new DatabaseService(context));
        try {
            getDbService().createDataBase();
            getDbService().openDataBase();
            Log.i(TAG, "Connected to Database");
        } catch (Exception e) {
            Log.e(TAG, "Problem with open db:" + e.getMessage());
            getDbService().fixDB();
        }
    }

    public Pattern findPatternByTitle(String patternTitle) throws NotFoundException {

        List<Pattern> patterns = getDbService().getAllPatterns();
        for (Pattern p : patterns) {
            if (p.getTitle().equalsIgnoreCase(patternTitle)) {
                return p;
            }
        }
        Log.w(TAG, "Pattern not found in findPatternByTitle()");
        throw new NotFoundException("Pattern not found");
    }

    public Chat findChatByTitle(String chatTitle) throws NotFoundException {
        Log.i(TAG, "finding Chat By Title");
        List<Chat> chats = getDbService().getAllChats();
        for (Chat chat : chats) {
            if (chat.getTitle().equalsIgnoreCase(chatTitle)) {
                return chat;
            }
        }
        Log.w(TAG, "Chat not found in findChatByTitle()");
        throw new NotFoundException("Chat not found");
    }

    public String[] getCultureInfoAsWordList(List<CultureInfoItem> cultureInfoItems) {
        ArrayList<String> wordList = new ArrayList<>();
        for (CultureInfoItem cii : cultureInfoItems) {
            wordList.add(cii.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public String[] getChatsAsWordList(List<Chat> chatList) throws NullListException {
        if (chatList == null) {
            throw new NullListException();
        }
        ArrayList<String> wordList = new ArrayList<>();
        for (Chat chat : chatList) {
            wordList.add(chat.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public String[] getLessonsAsWordList(List<Lesson> lessons) {
        ArrayList<String> wordList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            wordList.add(lesson.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public String[] getGrammarTipsAsWordList(List<GrammarTipItem> grammarTipItems) {
        ArrayList<String> wordList = new ArrayList<>();
        for (GrammarTipItem gti : grammarTipItems) {
            wordList.add(gti.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    private static void setDictionary(Dictionary dictionary) {
        DatabaseManager.dictionary = dictionary;
    }

    public String[] getLinksAsWordList(List<LinkItem> linksItems) {
        ArrayList<String> wordList = new ArrayList<>();
        for (LinkItem item : linksItems) {
            wordList.add(item.getTitle());
        }
        return wordList.toArray(new String[wordList.size()]);
    }

    public String[] getProverbsAsWordList(List<ProverbsItem> proverbsItems) {
        ArrayList<String> wordList = new ArrayList<>();
        for (ProverbsItem proverbsItem : proverbsItems) {
            wordList.add(proverbsItem.getEnglish());
        }
        return wordList.toArray(new String[wordList.size()]);
    }
}
