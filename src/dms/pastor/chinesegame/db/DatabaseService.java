package dms.pastor.chinesegame.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dms.pastor.chinesegame.common.exceptions.NotFoundException;
import dms.pastor.chinesegame.data.CultureInfoItem;
import dms.pastor.chinesegame.data.GrammarTipItem;
import dms.pastor.chinesegame.data.LinkItem;
import dms.pastor.chinesegame.data.ProverbsItem;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.QA;
import dms.pastor.chinesegame.data.dictionary.Question;
import dms.pastor.chinesegame.data.dictionary.Sentence;
import dms.pastor.chinesegame.data.learning.chats.Chat;
import dms.pastor.chinesegame.data.learning.lessons.Lesson;
import dms.pastor.chinesegame.data.learning.patterns.Pattern;
import dms.pastor.chinesegame.utils.DomUtils;

import static java.lang.String.format;

/**
 * User: Dominik Symonowicz
 * Date: 18.08.13
 * Time: 17:46
 * A helper class to manage database creation and version management.
 */
public final class DatabaseService extends SQLiteOpenHelper {

    private static final String TAG = "DATABASE";
    private static final String SHIT_HAPPENS = "Shit happens during flushing or closing streams after copy database:\n";
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String DB_NAME = "dictdb";
    /**
     * TABLES ,
     * if you add any here,add to displayInfo() method too!
     */

    private static final String TABLE_CHATS = "Chats";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TABLE_CULTURE_INFO = "cultureinfo";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String TABLE_GRAMMAR_TIPS = "grammartips";
    private static final String TABLE_LESSONS = "lessons";
    private static final String TABLE_PATTERNS = "patterns";
    private static final String TABLE_QA = "qa";
    private static final String TABLE_QAS_FOR_CHAT = "qa4chat";
    private static final String TABLE_QUESTIONS = "questions";
    private static final String TABLE_SENTENCES = "sentences";
    private static final String TABLE_LINKS = "links";
    private static final String TABLE_PROVERBS = "proverbs";
    private static String databasePath;
    private final Context myContext;
    private SQLiteDatabase myDataBase;
    private boolean ok = false;

    @SuppressLint("SdCardPath")
    DatabaseService(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        databasePath = "/data/data/" + myContext.getPackageName() + "/databases/";

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    void createDataBase() {
        Log.i(TAG, "creating database...");
        boolean dbExist = checkDataBase();

        if (dbExist) {
            updateDataBase();

        } else {
            this.getReadableDatabase();
            copyDataBase();
        }
    }

    private void updateDataBase() {
        Log.i(TAG, "Updating database...");
        deleteDB(databasePath + DB_NAME);
        copyDataBase();
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        Log.i(TAG, "Checking database...");
        SQLiteDatabase checkDB = null;

        try {
            String myPath = databasePath + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.w(TAG, "Exception during checkDataBase()" + e.getMessage());
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    private void copyDataBase() {
        Log.i(TAG, "Coping database...");
        InputStream myInput = null;
        OutputStream myOutput = null;
        String outFileName = databasePath + DB_NAME;
        try {
            myInput = myContext.getAssets().open(DB_NAME);
            myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            Log.i(TAG, "Database copied");
            ok = true;
        } catch (IOException ioe) {
            Log.e(TAG, "Problem with copy database to device due:" + ioe.getMessage());
            ok = false;
        } finally {
            try {
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException ioe) {
                Log.e(TAG, SHIT_HAPPENS + ioe.getMessage());
            } catch (NullPointerException npe) {
                Log.w(TAG, SHIT_HAPPENS + npe.getMessage());
            }

            try {
                if (myOutput != null) {
                    myOutput.flush();
                    myOutput.close();
                }
            } catch (IOException ioe) {
                Log.e(TAG, SHIT_HAPPENS + ioe.getMessage());
            } catch (NullPointerException npe) {
                Log.w(TAG, SHIT_HAPPENS + npe.getMessage());
            }

            try {
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException ioe) {
                Log.w(TAG, SHIT_HAPPENS + ioe.getMessage());
            }
        }
    }

    void openDataBase() throws SQLException {
        Log.i(TAG, "Opening the database...");
        String myPath = databasePath + DB_NAME;
        if (myDataBase == null) {
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
    }

    @SuppressWarnings("ConstantConditions")
    void fixDB() {
        Log.i(TAG, "Fixing the database...");
        boolean deleted = deleteDB(databasePath + DB_NAME);
        if (deleted) {
            copyDataBase();
        } else {
            Log.e(TAG, "Something went terrible wrong. Unable to fix problem with Database");
            DomUtils.msg(myContext, "Unable to delete database");
        }

    }

    @SuppressWarnings("ConstantConditions")
    private boolean deleteDB(String dbName) {
        Log.i(TAG, "Deleting the database...");
        File file = new File(dbName);
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean deleted = false;
        deleted |= file.delete();
        Log.w(TAG, deleted ? "DB file deleted." : "Unable to delete DB file");
        deleted |= new File(file.getPath() + "-journal").delete();
        Log.w(TAG, deleted ? "DB journal file deleted." : "Unable to delete DB journal file");
        deleted |= new File(file.getPath() + "-shm").delete();
        Log.w(TAG, deleted ? "DB shm file deleted." : "Unable to delete DB shm file");
        deleted |= new File(file.getPath() + "-wal").delete();
        Log.w(TAG, deleted ? "DB wal file deleted." : "Unable to delete DB wal file");

        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            try {
                final FileFilter filter = new FileFilter() {
                    @Override
                    public boolean accept(File candidate) {
                        return candidate.getName().startsWith(prefix);
                    }
                };
                for (File masterJournal : dir.listFiles(filter)) {
                    deleted |= masterJournal.delete();
                    Log.w(TAG, deleted ? "DB masterJournal file deleted." : "Unable to delete DB masterJournal file");
                }
            } catch (NullPointerException npe) {
                Log.w(TAG, "deleteDB was unable to delete masterJournal due: " + npe.getMessage());
                return false;
            }
        }
        return deleted;
    }

    @Override
    public synchronized void close() {
        Log.d(TAG, "Disconnecting from database");
        if (myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Fascinating. Unused onCreate() method called.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, format("Fascinating. Unused onUpdate() method called. oldVersion value:%d newVersion value::%d", oldVersion, newVersion));
    }

    public List<Question> getAllQuestions() {
        Log.i(TAG, "Getting all questions...");
        List<Question> questionList = new ArrayList<>();

        String selectQuery = "SELECT * FROM Questions";
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(Integer.parseInt(cursor.getString(0)));
                question.setCharacter(cursor.getString(1));
                question.setPinyin(cursor.getString(2));
                question.setEnglish(cursor.getString(3));
                question.setPolish(cursor.getString(4));
                question.setNotes(cursor.getString(5));
                if (question.isValidated()) {
                    questionList.add(question);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return questionList;
    }

    public Question getQuestion(int index) {
        Log.i(TAG, "Get question from index: " + index);
        if (index <= 0) {
            return Question.getEmptyQuestion();
        }
        Question question;
        String selectQuery = format(Locale.ENGLISH, "SELECT  * FROM %s WHERE _id = %d", TABLE_QUESTIONS, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            question = new Question();
            question.setId(Integer.parseInt(cursor.getString(0)));
            question.setCharacter(cursor.getString(1));
            question.setPinyin(cursor.getString(2));
            question.setEnglish(cursor.getString(3));
            question.setPolish(cursor.getString(4));
            question.setNotes(cursor.getString(5));

            if (question.isValidated()) {
                cursor.close();
                return question;
            }
        }
        cursor.close();
        return Question.getEmptyQuestion();
    }

    private int getCount(final String what) {
        Log.i(TAG, "Get count for : " + what);
        String countQuery = "SELECT  * FROM " + what;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int counter = cursor.getCount();
        cursor.close();
        return counter;
    }

    public Sentence getSentence(int index) {
        Log.i(TAG, "Get sentence from index: " + index);
        if (index <= 0) {
            return Sentence.getEmptySentence();
        }
        Sentence sentence;
        String selectQuery = format(Locale.ENGLISH, "SELECT  * FROM %s WHERE _id = %d", TABLE_SENTENCES, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            sentence = new Sentence();
            sentence.setId(Integer.parseInt(cursor.getString(0)));
            sentence.setCharacter(cursor.getString(1));
            sentence.setPinyin(cursor.getString(2));
            sentence.setEnglish(cursor.getString(3));
            sentence.setPolish(cursor.getString(4));
            sentence.setNotes(cursor.getString(5));
            if (sentence.isValidated()) {
                cursor.close();
                return sentence;
            }
        }
        cursor.close();
        return Sentence.getEmptySentence();
    }

    public List<Sentence> getAllSentences() {
        Log.i(TAG, "Get all sentences");
        List<Sentence> sentenceList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENTENCES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(0)) != -1) {
                    Sentence sentence = new Sentence();
                    sentence.setId(Integer.parseInt(cursor.getString(0)));
                    sentence.setCharacter(cursor.getString(1));
                    sentence.setPinyin(cursor.getString(2));
                    sentence.setEnglish(cursor.getString(3));
                    sentence.setPolish(cursor.getString(4));
                    sentence.setNotes(cursor.getString(5));
                    if (sentence.isValidated()) {
                        sentenceList.add(sentence);
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return sentenceList;
    }

    @SuppressWarnings("UnusedDeclaration")
    public Pattern getPattern(int index) throws NotFoundException {
        Log.i(TAG, "Get pattern for index: " + index);
        Pattern pattern;
        String selectQuery = format(Locale.ENGLISH, "SELECT  * FROM %s WHERE _id = %d", TABLE_PATTERNS, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            pattern = new Pattern();
            pattern.setId(Integer.parseInt(cursor.getString(0)));
            pattern.setTitle(cursor.getString(1));
            pattern.setDescription(cursor.getString(2));
            pattern.setPatternExample(cursor.getString(3));
            pattern.setExample1(cursor.getString(4));
            pattern.setExample1(cursor.getString(5));
            pattern.setExample1(cursor.getString(6));
            pattern.setExample1(cursor.getString(7));
            pattern.setExample1(cursor.getString(8));
            if (pattern.isValidated()) {
                cursor.close();
                return pattern;
            }
        }
        cursor.close();
        throw new NotFoundException("Pattern not found");
    }

    public List<Pattern> getAllPatterns() {
        Log.i(TAG, "Get all patterns");
        List<Pattern> patternList = new ArrayList<>();
        Pattern pattern;
        String selectQuery = SELECT_ALL_FROM + TABLE_PATTERNS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(0)) != -1) {
                    pattern = new Pattern();
                    pattern.setId(Integer.parseInt(cursor.getString(0)));
                    pattern.setTitle(cursor.getString(1));
                    pattern.setDescription(cursor.getString(2));
                    pattern.setPatternExample(cursor.getString(3));
                    switch (Integer.parseInt(cursor.getString(4))) {
                        case 0:
                            pattern.setExample1(getQuestion(Integer.valueOf(cursor.getString(5))).toString());
                            pattern.setExample2(getQuestion(Integer.valueOf(cursor.getString(6))).toString());
                            pattern.setExample3(getQuestion(Integer.valueOf(cursor.getString(7))).toString());
                            pattern.setExample4(getQuestion(Integer.valueOf(cursor.getString(8))).toString());
                            pattern.setExample5(getQuestion(Integer.valueOf(cursor.getString(9))).toString());
                            break;
                        case 1:
                            pattern.setExample1(getSentence(Integer.valueOf(cursor.getString(5))).toString());
                            pattern.setExample2(getSentence(Integer.valueOf(cursor.getString(6))).toString());
                            pattern.setExample3(getSentence(Integer.valueOf(cursor.getString(7))).toString());
                            pattern.setExample4(getSentence(Integer.valueOf(cursor.getString(8))).toString());
                            pattern.setExample5(getSentence(Integer.valueOf(cursor.getString(9))).toString());
                            break;
                        default:
                            Log.w(TAG, pattern.getTitle() + "has unknown example type");
                            break;

                    }
                    if (pattern.isValidated()) {
                        patternList.add(pattern);
                    }
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return patternList;
    }

    public List<Chat> getAllChats() throws NotFoundException {
        Log.i(TAG, "Loading all chats..");
        List<Chat> chatList = new ArrayList<>();
        Chat chat;

        String selectQuery = "SELECT  * FROM " + TABLE_CHATS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Dictionary dictionary = Dictionary.getDictionary();

        if (cursor.moveToFirst()) {
            do {
                chat = new Chat();
                int id = cursor.getInt(0);
                chat.setId(id);
                chat.setTitle(cursor.getString(1));
                chat.setInformation(cursor.getString(2));
                chat.setWordList(dictionary.getWordsFromCategoryFromDictionary(cursor.getString(3).split("~~")));
                List<QA> qas = getAllQAsForChat(id);
                chat.setQaList(qas);

                if (chat.isValidated()) {
                    chatList.add(chat);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return chatList;
    }

    public CultureInfoItem getCLI(int index) throws NotFoundException {
        Log.i(TAG, "Loading culture info for index: " + index);
        CultureInfoItem cultureInfoItem;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE _id = %d", TABLE_CULTURE_INFO, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            cultureInfoItem = new CultureInfoItem();
            cultureInfoItem.setId(Integer.parseInt(cursor.getString(0)));
            cultureInfoItem.setTitle(cursor.getString(1));
            cultureInfoItem.setInfo(cursor.getString(2));
            cultureInfoItem.setNotes(cursor.getString(3));
            if (cultureInfoItem.isValidated()) {
                cursor.close();
                return cultureInfoItem;
            }
        }
        cursor.close();
        throw new NotFoundException("Culture info not found");
    }

    public List<CultureInfoItem> getAllCultureInfo() {
        Log.i(TAG, "get all culture info");
        List<CultureInfoItem> ciiList = new ArrayList<>();
        CultureInfoItem cii;
        String selectQuery = "SELECT * FROM " + TABLE_CULTURE_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                cii = new CultureInfoItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                if (cii.isValidated()) {
                    ciiList.add(cii);
                } else {
                    Log.e(TAG, "Database is corrupted or data is invalid.");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return ciiList;
    }

    public List<Lesson> getAllLessons() {
        Log.i(TAG, "get all lessons");
        List<Lesson> lessonList = new ArrayList<>();
        Lesson lesson;
        String selectQuery = "SELECT * FROM " + TABLE_LESSONS;
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                lesson = new Lesson();
                lesson.setId(Integer.parseInt(cursor.getString(0)));
                lesson.setTitle(cursor.getString(1));
                lesson.setGroup(cursor.getString(2).split("~~"));
                DatabaseManager.getDbManager(null);
                lesson.setWords(Dictionary.getDictionary().getWordsFromCategoryFromDictionary(lesson.getGroup()));
                lesson.setLessonContent(cursor.getString(3));
                lesson.setNotes(cursor.getString(4));

                if (lesson.isValidated()) {
                    lessonList.add(lesson);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessonList;
    }


    private List<QA> getAllQAsForChat(int id) throws NotFoundException {
        Log.i(TAG, format("ChatId  is:%d", id));
        List<QA> qaList = new ArrayList<>();
        QA qa;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE chat_id = %d", TABLE_QAS_FOR_CHAT, id);
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                qa = getQA(cursor.getInt(2));
                qaList.add(qa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return qaList;
    }

    private QA getQA(int index) throws NotFoundException {
        Log.i(TAG, "get QA for index:" + index);
        QA qa;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE _id = %d", TABLE_QA, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            qa = new QA();
            Log.w(TAG, "Setting question");
            qa.setQuestion(getQuestion(Integer.parseInt(cursor.getString(1))));
            Log.w(TAG, "Setting answer");
            qa.setAnswer(getSentence(Integer.parseInt(cursor.getString(2))));
            return qa;
        }
        cursor.close();
        throw new NotFoundException("QA not found");
    }


    public String displayInfoAboutDB() {
        StringBuilder sb = new StringBuilder("DB INFO:\n");
        try {
            sb.append("\nCHATS: ").append(getCount("chats"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH CHAT:").append(e.getMessage());
        }

        try {
            //noinspection SpellCheckingInspection
            sb.append("\nCULTURE INFO: ").append(getCount("cultureinfo"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH INFO:").append(e.getMessage());
        }

        try {
            //noinspection SpellCheckingInspection
            sb.append("\nGRAMMAR TIPS: ").append(getCount("grammartips"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH GRAMMAR TIPS:").append(e.getMessage());
        }

        try {
            sb.append("\nLESSONS: ").append(getCount("lessons"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH LESSONS:").append(e.getMessage());
        }

        try {
            sb.append("\nPATTERNS: ").append(getCount("patterns"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH PATTERNS:").append(e.getMessage());
        }

        try {
            sb.append("\nQA: ").append(getCount("qa"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH QA:").append(e.getMessage());
        }

        try {
            sb.append("\nQA (4CHAT):: ").append(getCount("qa4chat"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH QA (4CHAT):").append(e.getMessage());
        }

        try {
            sb.append("\nQUESTIONS: ").append(getCount("questions"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH QUESTIONS:").append(e.getMessage());
        }

        try {
            sb.append("\nSENTENCES: ").append(getCount("sentences"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH SENTENCES:").append(e.getMessage());
        }

        try {
            sb.append("\nWORDS: ").append(getCount("words"));
        } catch (Exception e) {
            sb.append("PROBLEMS WITH WORDS:").append(e.getMessage());
        }


        return sb.toString();
    }

    public List<GrammarTipItem> getAllGrammarTips() {
        Log.i(TAG, "get all grammar tips");
        List<GrammarTipItem> gtiList = new ArrayList<>();
        GrammarTipItem gti;
        String selectQuery = "SELECT * FROM " + TABLE_GRAMMAR_TIPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                gti = new GrammarTipItem();
                gti.setId(Integer.parseInt(cursor.getString(0)));
                gti.setTitle(cursor.getString(1));
                gti.setInfo(cursor.getString(2));
                gti.setNotes(cursor.getString(3));
                if (gti.isValidated()) {
                    gtiList.add(gti);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return gtiList;
    }

    public GrammarTipItem getGrammarTip(int index) throws NotFoundException {
        Log.i(TAG, "get grammar tip for index:" + index);
        GrammarTipItem grammarTipItem;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE _id = %d", TABLE_GRAMMAR_TIPS, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            grammarTipItem = new GrammarTipItem();
            grammarTipItem.setId(Integer.parseInt(cursor.getString(0)));
            grammarTipItem.setTitle(cursor.getString(1));
            grammarTipItem.setInfo(cursor.getString(2));
            grammarTipItem.setNotes(cursor.getString(3));
            if (grammarTipItem.isValidated()) {
                cursor.close();
                return grammarTipItem;
            }
        }
        cursor.close();
        throw new NotFoundException("Learning tip not found");
    }

    public List<LinkItem> getAllLinks() {
        Log.i(TAG, "get all links");
        List<LinkItem> gtiList = new ArrayList<>();
        LinkItem item;
        String selectQuery = "SELECT * FROM " + TABLE_LINKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                item = new LinkItem();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setWww(cursor.getString(2));
                item.setNotes(cursor.getString(3));
                if (item.isValidated()) {
                    gtiList.add(item);
                } else {
                    Log.e(TAG, "Validation failed for \'" + DomUtils.getUnknownWhenNullString(cursor.getString(1)) + "\'");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return gtiList;
    }

    public LinkItem getLink(int index) throws NotFoundException {
        Log.i(TAG, "get link for index:" + index);
        LinkItem grammarTipItem;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE _id = %d", TABLE_LINKS, index);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            grammarTipItem = new LinkItem();
            grammarTipItem.setId(Integer.parseInt(cursor.getString(0)));
            grammarTipItem.setTitle(cursor.getString(1));
            grammarTipItem.setWww(cursor.getString(2));
            grammarTipItem.setNotes(cursor.getString(3));
            if (grammarTipItem.isValidated()) {
                cursor.close();
                return grammarTipItem;
            }
        }
        cursor.close();
        throw new NotFoundException("Learning tip not found");
    }

    public List<ProverbsItem> getAllProverbs() {
        Log.i(TAG, "get all proverbs");
        List<ProverbsItem> gtiList = new ArrayList<>();
        ProverbsItem item;
        String selectQuery = "SELECT * FROM " + TABLE_PROVERBS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                item = new ProverbsItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                Log.d(TAG, item.toString());
                if (item.isValidated()) {
                    gtiList.add(item);
                } else {
                    Log.e(TAG, "Validation for \'" + DomUtils.getUnknownWhenNullString(cursor.getString(1)) + "\'");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return gtiList;
    }

    public ProverbsItem getProverb(int position) throws NotFoundException {
        Log.i(TAG, "get proverb for index:" + position);
        ProverbsItem proverbsItem;
        String selectQuery = format(Locale.ENGLISH, "SELECT * FROM %s WHERE _id = %d", TABLE_PROVERBS, position);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            proverbsItem = new ProverbsItem();
            proverbsItem.setId(Integer.parseInt(cursor.getString(0)));
            proverbsItem.setChinese(cursor.getString(1));
            proverbsItem.setPinyin(cursor.getString(2));
            proverbsItem.setEnglish(cursor.getString(3));
            proverbsItem.setPolish(cursor.getString(4));
            proverbsItem.setMeaning(cursor.getString(5));
            proverbsItem.setNotes(cursor.getString(6));

            if (proverbsItem.isValidated()) {
                cursor.close();
                return proverbsItem;
            }
        }
        cursor.close();
        throw new NotFoundException("Learning tip not found");
    }

    boolean isOK() {
        return ok;
    }

}
