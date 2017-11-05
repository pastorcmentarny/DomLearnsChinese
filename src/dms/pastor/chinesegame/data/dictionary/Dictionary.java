package dms.pastor.chinesegame.data.dictionary;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;

import static dms.pastor.chinesegame.Config.NEW_LINE;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 15/11/2012
 */
public final class Dictionary {
    private static final String TAG = "DICTIONARY";
    private static final String WORDS = " words.";
    private static Dictionary dictionary = null;
    private ArrayList<Word> wordsList = new ArrayList<>();
    private boolean status;

    private Dictionary() {
    }

    public static synchronized Dictionary getDictionary() {

        if (dictionary == null) {
            dictionary = new Dictionary();
        }
        return dictionary;
    }

    public static Dictionary recreateDictionary() {
        Log.i(TAG, "dictionary cleared.");
        dictionary = null;
        dictionary = getDictionary();
        return dictionary;
    }

    private static String getLine(String line) {
        return line != null ? "[" + line + "]" : "line is empty.";
    }

    private static String lvl(int level) {
        return format(ENGLISH, "Lvl %d: ", level);
    }

    private static String getPercentageOfAllWordsAsString(double words) {
        double value = (words * 100 / dictionary.getWordsList().size());
        DecimalFormat df = new DecimalFormat("##.##");
        value = Double.valueOf(df.format(value));
        return format(ENGLISH, "%s%%", valueOf(value));
    }

    private static void closeReaderQuietly(Reader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            Log.w(TAG, "Unable to close  due " + e.getMessage(), e);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public ArrayList<Word> getWordsList() {
        Log.d(TAG, "Dictionary size:" + wordsList.size());
        return wordsList;
    }

    public Result readDictionaryFromFile(Context context, int resourceId, String[] requestedCategories) {
        Log.i(TAG, "Loading words to dictionary from file");
        wordsList = new ArrayList<>();
        InputStream iStream;
        try {
            iStream = context.getResources().openRawResource(resourceId);
        } catch (Resources.NotFoundException e) {
            return new Result(false, context.getString(R.string.e_file_read) + e.getMessage());
        }

        DataInputStream in = new DataInputStream(iStream);
        BufferedReader br;
        InputStreamReader isr;
        String strLine;
        String[] data;
        String[] wordCategoriesList = null;
        Word word;
        int nr = 0;
        try {
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);
            while ((strLine = br.readLine()) != null) {
                if (!strLine.startsWith(Config.IGNORED_WORD)) {
                    data = strLine.split(Config.COLUMN_SEPERATOR);
                    try {
                        if (requestedCategories != null) {
                            wordCategoriesList = data[7].split(Config.GROUP_SEPERATOR);
                        }
                        String[] temp = data[7].split(Config.GROUP_SEPERATOR);
                        int difficultly = DomUtils.parseIntNullSafe(data[9], 9);
                        word = new Word(Integer.parseInt(data[1]), data[2], data[3], Integer.parseInt(data[4]), data[5], data[6], temp, data[8], difficultly);
                        if (word.isValid()) {
                            nr = addWordToWordList(requestedCategories, wordCategoriesList, word, nr); //TODO improve nr
                        } else {
                            Log.e(TAG, "Word is corrupted.(Line:" + (nr - 1) + ".It is something wrong with Dictionary." + getLine(strLine));
                            return new Result(false, "Word is corrupted(Line:" + (nr - 1) + ")\n.It is something wrong with Dictionary." + getLine(strLine));
                        }
                    } catch (NumberFormatException nfe) {
                        setDBStatus(false);
                        String msg = context.getString(R.string.e_read_file_problem_in_line) + (nr - 1) + ")\n(NumberFormatException): " + nfe.getMessage() + getLine(strLine);
                        Log.w(TAG, msg);
                        return new Result(false, msg);
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        setDBStatus(false);
                        String msg = context.getString(R.string.e_read_file_problem_in_line) + (nr - 1) + ")\n(ArrayIndexOutOfBoundsException): " + aioobe.getMessage() + getLine(strLine);
                        Log.w(TAG, msg);
                        return new Result(false, msg);
                    }
                } else {
                    Log.w("Dictionary entry:", strLine);
                }
            }
        } catch (IOException e) {
            setDBStatus(false);
            return new Result(false, context.getString(R.string.e_read_file_problem_in_line) + nr + ")\n(IOException):" + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            setDBStatus(false);
            return new Result(false, context.getString(R.string.dict_data_corrupted) + nr + ")\n(ArrayIndexOutOfBoundsException)" + aioobe.getMessage());
        }
        closeReaderQuietly(isr);
        closeReaderQuietly(br);

        try {
            in.close();
        } catch (IOException e) {
            Log.w(TAG, context.getString(R.string.e_closing_stream_problem) + "(DataInputStream)" + e.getMessage());
            setDBStatus(false);
            return new Result(false, "Problem with \"closes this stream\"  file.\n" + e.getMessage());
        }
        setDBStatus(true);
        return new Result(true, context.getString(R.string.msg_dict_loaded));
    }

    private int addWordToWordList(String[] requestedCategories, String[] wordCategoriesList, Word word, int nr) {
        if (requestedCategories != null) {
            for (String requestedCategory : requestedCategories) {
                for (String wordCategory : wordCategoriesList) {
                    if (wordCategory.equals(requestedCategory)) {
                        wordsList.add(word);
                        nr++;
                    }
                }
            }
        } else {
            wordsList.add(word);
            nr++;
        }
        return nr;
    }

    public Word getWordFromDictionary(int id) {
        id++;
        for (Word word : wordsList) {
            if (word.getId() == id) {
                return word;
            }
        }
        Log.w(TAG, "Word not found in dictionary for id:" + id);
        return null; //TODO replace with noneWord
    }

    public ArrayList<Word> getWordsFromCategoryFromDictionary(String[] requestedCategories) {
        Log.i(TAG, "Getting words from dictionary for specified category(categories).");
        ArrayList<Word> result = new ArrayList<>();
        for (Word word : wordsList) {
            for (String wordCategories : word.getGroups()) {
                addWordsFromRequestedCategories(requestedCategories, result, word, wordCategories);
            }
        }
        Log.i(TAG, valueOf(result.size()));
        return result;
    }

    private static void addWordsFromRequestedCategories(String[] requestedCategories, ArrayList<Word> result, Word word, String wordCategories) {
        for (String category : requestedCategories) {
            if (category.equalsIgnoreCase(wordCategories)) {
                result.add(word);
            }
        }
    }

    public String[] generateWordList() {
        Log.i(TAG, "Generating word list");
        ArrayList<String> words = new ArrayList<>();
        int counter = 1;
        for (Word word : wordsList) {
            words.add(format("%s. %s", valueOf(counter), word.toShortString()));
            counter++;
        }
        return words.toArray(new String[words.size()]);
    }

    public ArrayList<Word> getWordsForLevel(int level) {
        Log.i(TAG, "Getting words from dictionary for specified category(categories).");
        ArrayList<Word> result = new ArrayList<>();
        if (level <= 5) {
            for (Word word : wordsList) {
                if (word.getDifficulty() <= level) {
                    result.add(word);
                }
            }
        } else if (level == 6) {
            getWordsForDifficultyAbove(1, level, result);
        } else if (level == 7) {
            getWordsForDifficultyAbove(2, level, result);
        } else if (level == 8) {
            getWordsForDifficultyAbove(3, level, result);
        }
        return result;
    }

    public HashSet<Word> findAllWords(String searchQuery) {
        final HashSet<Word> words = new HashSet<>();
        Log.d(TAG, "Looking for word:" + searchQuery);
        if (wordsList == null) {
            Log.e(TAG, "Cannot search due lack of dictionary.Error?");
            throw new DictionaryException();
        } else {
            Log.i(TAG, "Dictionary size:" + wordsList.size());
        }

        for (Word word : wordsList) {
            if (word.getChineseCharacter().contains(searchQuery)) {
                words.add(word);
            }
            if (word.getWordInEnglish().toLowerCase().contains(searchQuery.toLowerCase())) {
                words.add(word);
            }
            //TODO add search for group
        }
        return words;
    }

    public String getWordsPerLevelStats() {
        StringBuilder sb = new StringBuilder("");
        int l1 = 0;
        int l2 = 0;
        int l3 = 0;
        int l4 = 0;
        int l5 = 0;
        int l6 = 0;
        int l7 = 0;
        int l8 = 0;
        for (Word w : wordsList) {
            switch (w.getDifficulty()) {
                case 1:
                    l1++;
                    break;
                case 2:
                    l2++;
                    break;
                case 3:
                    l3++;
                    break;
                case 4:
                    l4++;
                    break;
                case 5:
                    l5++;
                    break;
                case 6:
                    l6++;
                    break;
                case 7:
                    l7++;
                    break;
                case 8:
                    l8++;
                    break;
            }

        }
        sb.append(lvl(1)).append(l1).append(WORDS).append(getPercentageOfAllWordsAsString(l1)).append(NEW_LINE);
        sb.append(lvl(2)).append(l2).append(WORDS).append(getPercentageOfAllWordsAsString(l2)).append(NEW_LINE);
        sb.append(lvl(3)).append(l3).append(WORDS).append(getPercentageOfAllWordsAsString(l3)).append(NEW_LINE);
        sb.append(lvl(4)).append(l4).append(WORDS).append(getPercentageOfAllWordsAsString(l4)).append(NEW_LINE);
        sb.append(lvl(5)).append(l5).append(WORDS).append(getPercentageOfAllWordsAsString(l5)).append(NEW_LINE);
        sb.append(lvl(6)).append(l6).append(WORDS).append(getPercentageOfAllWordsAsString(l6)).append(NEW_LINE);
        sb.append(lvl(7)).append(l7).append(WORDS).append(getPercentageOfAllWordsAsString(l7)).append(NEW_LINE);
        sb.append(lvl(8)).append(l8).append(WORDS).append(getPercentageOfAllWordsAsString(l8)).append(NEW_LINE);
        sb.append("Total words: ").append(wordsList.size());
        return sb.toString();
    }

    public int getAllDictionarySize() {
        return wordsList.size();
    }

    private void setDBStatus(boolean status) {
        this.status = status;
    }

    public boolean isDBOk() {
        return status;
    }

    public Word findWordById(int id) {
        for (Word word : wordsList) {
            if (word.getId() == id) {
                return word;
            }
        }
        throw new WordNotFoundException(id);
    }

    private void getWordsForDifficultyAbove(int difficulty, int level, ArrayList<Word> result) {
        for (Word word : wordsList) {
            if (word.getDifficulty() <= level && word.getDifficulty() > difficulty) {
                result.add(word);
            }
        }
    }

}
