package dms.pastor.chinesegame.data.game.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.utils.Result;

import static dms.pastor.chinesegame.utils.DomUtils.displayError;
import static dms.pastor.chinesegame.utils.DomUtils.getUnknownWhenNullString;
import static dms.pastor.chinesegame.utils.DomUtils.parseIntNullSafe;
import static dms.pastor.chinesegame.utils.DomUtils.parseLongNullSafe;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Collections.sort;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 16/11/2012
 */
public final class HighScore {
    private static final String TAG = Config.TAG_PREFIX + "HighScore";
    private static HighScore highScore;
    private final Context context;

    private Result status;

    private ArrayList<Score> hsAdventure;
    private ArrayList<Score> hsSapper;
    private ArrayList<Score> hsDictinary;

    private HighScore(Context context) {
        this.context = context;
        status = new Result(false, "UNKNOWN");
        loadHighScores();
        if (status.isFail()) {
            displayError(context, status.getMessage());
        }
        sortAllHighScores();
    }

    /**
     * get new instance of high score,if was not created.
     *
     * @return HighScore if was not instanced.
     */
    public static synchronized HighScore getNewHighScore(Context context) {

        if (highScore == null) {
            highScore = new HighScore(context);
        }
        return highScore;
    }

    /**
     * get new instance of high score,if was not created.
     *
     * @return HighScore if was not instanced.
     */
    public static synchronized HighScore getHighScore() {
        return highScore;
    }

    public static Result encrypt(Context context, String path) {
        Log.i(TAG, format("encrypting for %s", path));
        Result r = new Result(true);
        File backupFolder = Config.BACKUP_FOLDER;
        if (!backupFolder.mkdirs()) {
            new Result(false, "unable to create  folder for backup");
        }


        Log.i(TAG, format("performing backup for %s", path));
        File dst = new File(backupFolder, path);

        File src = new File(Config.HIGH_SCORE_FILE_PATH);

        try {
            encrypt(context.openFileInput(String.valueOf(src)), new FileOutputStream(dst));
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.whoops) + e.getMessage());
        }
        return r;
    }

    //TODO remove this crap
    @Deprecated
    private static void encrypt(FileInputStream fis, FileOutputStream fos) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(Config.getKey().getBytes(), Config.CIPHER_TYPE);
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(Config.CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fis.close();
    }

    @Deprecated
    public static Result decrypt(Context context, String path) {
        Log.i(TAG, format("performing decrypt for %s", path));
        Result r = new Result(true);
        File backupFolder = Config.BACKUP_FOLDER;
        if (!backupFolder.mkdirs()) {
            new Result(false, "unable to create  folder for backup");
        }

        Log.i(TAG, format("performing backup for %s", path));
        File dst = new File(backupFolder, path);

        File src = new File(Config.HIGH_SCORE_FILE_PATH);

        try {
            decrypt(new FileInputStream(dst), context.openFileOutput(String.valueOf(src), Context.MODE_PRIVATE));
        } catch (Exception e) {
            Log.e(TAG, "Unable to decrypt backup data due " + e.getMessage(), e);
        }
        return r;
    }

    private static void decrypt(FileInputStream fis, FileOutputStream fos) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(Config.getKey().getBytes(), Config.CIPHER_TYPE);
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(Config.CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    private static Score addScore(String[] data) {
        int five, six;
        long dateStamp;
        five = parseIntNullSafe(data[5], 0);
        six = parseIntNullSafe(data[6], 0);
        dateStamp = parseLongNullSafe(data[7], 0);
        String difficulty = getUnknownWhenNullString(data[8]);
        parseIntNullSafe(data[6], 0);
        return new Score(data[1], parseInt(data[2]), parseInt(data[3]), data[4], five, six, dateStamp, difficulty);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Object clone() throws CloneNotSupportedException {
        Log.e(TAG, "Clone not supported exception!");
        throw new CloneNotSupportedException();
    }

    private void loadHighScores() {
        loadHighScoreFor(Config.HIGH_SCORE_FILE_PATH, GameType.ADVENTURE);
        if (status.isFail()) {
            return;
        }
        loadHighScoreFor(Config.HIGH_SCORE_SAPPER_FILE_PATH, GameType.SAPPER);
        if (status.isFail()) {
            return;
        }
        loadHighScoreFor(Config.HIGH_SCORE_DICTIONARY_FILE_PATH, GameType.DICTIONARY_TEST);

    }

    private void loadHighScoreFor(String filePath, GameType gameType) {
        Log.i(TAG, "Loading high-scores for file " + filePath);
        ArrayList<Score> scores = new ArrayList<>();
        String strLine;
        String[] data;

        FileInputStream fis;
        DataInputStream in;
        BufferedReader br;
        InputStreamReader isr;


        if (isFileExist(filePath)) {
            try {
                fis = context.openFileInput(filePath);
            } catch (FileNotFoundException fnf) {
                Log.w(TAG, context.getString(R.string.error_unable_to_create_file) + fnf.getMessage());
                status.updateResultForFalse(context.getString(R.string.error_unable_to_create_file) + fnf.getMessage());
                return;

            }
        } else {
            if (!createHSFile(filePath)) {
                return;
            } else {
                try {
                    fis = context.openFileInput(filePath);
                } catch (FileNotFoundException fnf) {
                    Log.w(TAG, context.getString(R.string.error_unable_to_create_file) + fnf.getMessage());
                    status.updateResultForFalse("Unable to create file for high score due problems.Error:" + fnf.getMessage());
                    return;
                }
            }
        }

        in = new DataInputStream(fis);
        isr = new InputStreamReader(in);
        br = new BufferedReader(isr);
        try {
            while ((strLine = br.readLine()) != null) {
                data = strLine.split(";;");
                scores.add(addScore(data));
            }
        } catch (IOException e) {
            Log.w(TAG, "Problem with reading line due " + e.getMessage());
        } finally {


            try {
                br.close();
            } catch (IOException e) {
                Log.w(TAG, "Problem with reading line due (BufferedReader):" + e.getMessage());

            }
            try {
                isr.close();
            } catch (IOException e) {
                Log.w(TAG, "Problem with reading line due (InputStreamReader):" + e.getMessage());

            }
            try {
                in.close();
            } catch (IOException e) {
                Log.w(TAG, "Problem with reading line due (DataInputStream):" + e.getMessage());

            }
            try {
                fis.close();
            } catch (IOException e) {
                Log.w(TAG, "Problem with reading line due " + e.getMessage());
            }

        }

        switch (gameType) {
            case ADVENTURE:
                hsAdventure = scores;
                break;
            case SAPPER:
                hsSapper = scores;
                break;
            case DICTIONARY_TEST:
                hsDictinary = scores;
                break;
            default:
                status = new Result(false, "Unknown highscore list.. " + gameType.name());
                Log.e(TAG, "Unknown highscore list.." + gameType.name());
                return;
        }
        status = new Result(true, "High scores loaded");
    }

    private boolean isFileExist(String filePath) {
        File file = context.getFileStreamPath(filePath);
        return file.exists();
    }

    private boolean createHSFile(String filePath) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            if (isFileExist(filePath)) {
                Log.i(TAG, ":File " + filePath + " has been created.");
            } else {
                Log.w(TAG, "UNABLE to create file for " + filePath + ".");
            }


        } catch (IOException e) {
            Log.w(TAG, "UNABLE to create file for " + filePath + ".");
            status.updateResultForFalse("UNABLE to create file for " + filePath + ".");
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                Log.w(TAG, "UNABLE to close stream for  " + filePath + ".");
            }
        }

        return isFileExist(filePath);
    }

    public Result fixHighScores() {
        status = new Result(true);
        Log.i(TAG, "Fixing high score... ");
        recreateHSFile(Config.HIGH_SCORE_FILE_PATH);
        recreateHSFile(Config.HIGH_SCORE_SAPPER_FILE_PATH);
        loadHighScores();
        if (status.isSuccess()) {
            Log.i(TAG, "Highscore loaded after fix");
            saveHighScores();
        }
        return status;
    }

    //TODO this code is odd
    private Result saveHighScoreFor(GameType gameType) {
        Log.i(TAG, "Saving high scores for " + gameType.toString());
        ArrayList<Score> scores = getScoresFor(gameType);
        try {
            if (status.isFail() || scores != null) {
                new Result(false, "high score are not available at the moment.");
            }
            sortHighScoreFor(gameType);
            StringBuilder sb = new StringBuilder(Config.EMPTY_STRING);
            int counter = 1;
            ArrayList<Score> topScores = new ArrayList<>();
            for (Score score : scores) {
                if (counter <= Config.HIGH_SCORE_SIZE) {
                    sb.append(score.toString());
                    topScores.add(score);
                    counter++;
                }
            }
            OutputStreamWriter out = null;
            FileOutputStream fos;
            try {
                fos = context.openFileOutput(Config.getFilePathFor(gameType), Context.MODE_PRIVATE);
                out = new OutputStreamWriter(fos);
                out.write(sb.toString());
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
                status.set(false, "It was problem with saving high score to file. Error: " + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Problem with closing stream for save High Scores.\nError message:" + e.getMessage());
                    }
                }
            }
            return new Result(true, "high score saved.");
        } catch (NullPointerException npe) {
            Log.e(TAG, "IOException " + npe.getMessage());
            return new Result(false, "It was problem with saving high score to file. Error: " + npe.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unknown Exception " + e.getMessage());
            return new Result(false, "It was problem with saving high score to file. Error: " + e.getMessage());
        }
    }

    private void sortHighScoreFor(GameType gameType) {
        switch (gameType) {
            case ADVENTURE:
                sort(hsAdventure);
                break;
            case SAPPER:
                sort(hsSapper);
                break;
            case DICTIONARY_TEST:
                sort(hsDictinary);
            default:
                Log.w(TAG, "No sort for" + gameType.toString());
                break;
        }
    }

    private void saveHighScores() {
        Log.i(TAG, "Saving high scores");
        Result r = saveHighScoreFor(GameType.ADVENTURE);
        if (r.isFail()) {
            Log.w(TAG, r.getMessage());
            return;
        }
        r.update(saveHighScoreFor(GameType.SAPPER)); // saving hs for sapper and update result
        if (r.isFail()) {
            Log.w(TAG, r.getMessage());
        }
    }

    public int getCurrentPlaceFor(int score, GameType gameType) {
        Log.d(TAG, "Checking current place on highscore for score:" + score);
        if (status.isFail()) {
            Log.w(TAG, "No highscore");
            return 0;
        }
        switch (gameType) {
            case ADVENTURE:
                for (int i = 0; i < hsAdventure.size(); i++) {
                    if (score >= hsAdventure.get(i).getScore()) {
                        Log.d("Result", "Score:" + score + " HS score:" + hsAdventure.get(i).getScore() + " i:" + i);
                        return (i + 1);
                    }
                }
                return 0;
            case SAPPER:
                for (int i = 0; i < hsSapper.size(); i++) {
                    if (score >= hsSapper.get(i).getScore()) {
                        Log.d("Result", "Score:" + score + " HS score:" + hsSapper.get(i).getScore() + " i:" + i);
                        return (i + 1);
                    }
                }
                return 0;
            case DICTIONARY_TEST:
                for (int i = 0; i < hsDictinary.size(); i++) {
                    if (score >= hsDictinary.get(i).getScore()) {
                        Log.d("Result", "Score:" + score + " HS score:" + hsDictinary.get(i).getScore() + " i:" + i);
                        return (i + 1);
                    }
                }
                return 0;
            default:
                Log.w(TAG, "There is no highscore for " + gameType.name().toLowerCase());
                return 0;
        }
    }

    public void addToHighScore(Score score, GameType type) {
        if (score.getScore() <= 100) {
            return;
        }
        switch (type) {
            case ADVENTURE:
                if (hsAdventure != null) {
                    hsAdventure.add(score);
                }
                break;
            case SAPPER:
                if (hsSapper != null) {
                    hsSapper.add(score);
                }
                break;
            case DICTIONARY_TEST:
                if (hsDictinary != null) {
                    hsDictinary.add(score);
                }
                break;
        }
        saveHighScores();
    }

    public String displayTop10For(GameType gameType) {
        if (status.isFail()) {
            return "No high score available";
        }

        sortHighScoreFor(gameType);
        StringBuilder top10 = new StringBuilder(Config.EMPTY_STRING);
        int i = 1;
        ArrayList<Score> scores = getScoresFor(gameType);
        for (Score newScore : scores) {
            top10.append(i++).append(". ").append(newScore.asHighScore());
            if (i > 10) {
                break;
            }
        }
        return top10.toString();
    }

    public ArrayList<Score> getScoresFor(GameType gameType) {
        switch (gameType) {
            case ADVENTURE:
                return hsAdventure;
            case SAPPER:
                return hsSapper;
            case DICTIONARY_TEST:
                return hsDictinary;
            default:
                Log.e(TAG, "No high scores for:" + gameType.toString() + "App will crash..");
                return new ArrayList<>();
        }
    }

    public boolean isAvailable() {
        return status.isSuccess();
    }

    private void sortAllHighScores() {
        sort(hsAdventure);
        sort(hsSapper);
        sort(hsDictinary);
    }

    private void recreateHSFile(String path) {
        File file = new File(context.getFilesDir(), path);
        boolean isDeleted = file.delete();
        if (isDeleted) {

            Log.i(TAG, "Highscore file " + path + " has been deleted.");
            try {
                boolean isNewFileCreated = file.createNewFile();
                if (isNewFileCreated) {
                    Log.i(TAG, " NEW Highscore file" + path + "has been created.");

                } else {
                    Log.w(TAG, " NEW Highscore file" + path + "has been NOT created.");

                }
            } catch (IOException e) {
                Log.w(TAG, ": Unable to create File " + path + " due " + e.getMessage());
            }
        } else {
            Log.w(TAG, ": UNABLE to delete Highscore file " + path + ".");
        }
    }

    //TODO analyse this
    public Result backupHighScores(Context context) {
        saveHighScores();
        Result r = new Result(true);
        File root = android.os.Environment.getExternalStorageDirectory();
        File backupFolder = new File(root.getAbsolutePath() + "/domsBackups");
        if (!backupFolder.mkdirs()) {
            new Result(false, "Unable to create backup folder,so program is unable to save backup.");
        }

        String path = GameType.ADVENTURE.name() + "-" + Config.getCurrentDateAsString() + ".txt";
        Log.i(TAG, format("performing backup for %s", path));
        File dst = new File(backupFolder, path);
        try {
            File src = new File(Config.HIGH_SCORE_FILE_PATH);
            encrypt(context.openFileInput(String.valueOf(src)), new FileOutputStream(dst));
            encrypt(context.openFileInput(String.valueOf(src)), new FileOutputStream(new File(backupFolder, Config.HIGH_SCORE_FILE_PATH)));
        } catch (Exception e) {
            r.set(false, context.getString(R.string.whoops) + e.getMessage());
        }

        path = GameType.SAPPER.name() + "-" + Config.getCurrentDateAsString() + ".txt";
        Log.i(TAG, format("performing backup for %s", path));
        dst = new File(backupFolder, path);
        try {
            File src = new File(Config.HIGH_SCORE_SAPPER_FILE_PATH);
            encrypt(context.openFileInput(String.valueOf(src)), new FileOutputStream(dst));
            encrypt(context.openFileInput(String.valueOf(src)), new FileOutputStream(new File(backupFolder, Config.HIGH_SCORE_SAPPER_FILE_PATH)));
        } catch (Exception e) {
            r.setSuccess(false);
            r.addMessage(context.getString(R.string.whoops) + e.getMessage());
        }

        if (r.isSuccess()) {
            r.setMessage("Backup is done.");
        } else {
            r.addMessage("Backup wasn't done due error :(");
        }
        return r;
    }

    public Result restoreHighScores(Context context) {
        Result r = new Result(true);
        File root = android.os.Environment.getExternalStorageDirectory();
        File backupFolder = new File(root.getAbsolutePath() + "/domsBackups");

        String path = Config.HIGH_SCORE_FILE_PATH;
        Log.i(TAG, "performing restore for " + path);
        File dst = new File(backupFolder, path);
        try {
            File src = new File(Config.HIGH_SCORE_FILE_PATH);
            decrypt(new FileInputStream(dst), context.openFileOutput(String.valueOf(src), Context.MODE_PRIVATE));
        } catch (Exception e) {
            r.set(false, context.getString(R.string.whoops) + e.getMessage());
        }

        path = Config.HIGH_SCORE_SAPPER_FILE_PATH;
        Log.i(TAG, "performing restore for " + path);
        dst = new File(backupFolder, path);
        try {
            File src = new File(Config.HIGH_SCORE_SAPPER_FILE_PATH);
            decrypt(new FileInputStream(dst), context.openFileOutput(String.valueOf(src), Context.MODE_PRIVATE));
        } catch (Exception e) {
            r.setSuccess(false);
            r.addMessage(context.getString(R.string.whoops) + e.getMessage());
        }

        if (r.isSuccess()) {
            loadHighScores();
            r.setMessage("restore is done.");
        } else {
            r.addMessage("restore wasn't done due error :(");
        }
        return r;
    }


}
