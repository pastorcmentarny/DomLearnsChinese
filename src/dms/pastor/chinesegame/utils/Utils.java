package dms.pastor.chinesegame.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.db.DatabaseManager;

/**
 * User: Pastor
 * Date: 02.01.13
 * Time: 22:35
 */
public final class Utils {
    private static final String TXT = ".txt";

    private Utils() {
        //Utility classes should not have a public or default constructor.
    }

    //INFO: if you add new info don't forget add commandline do the list
    @SuppressWarnings("SpellCheckingInspection")
    //as they contains commands that they are not grammar friendly
    public static String getInfoFor(Context context, String cmd) {
        if (DomUtils.isStringNotEmpty(cmd)) {
            SharedPreferences settings;
            settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = settings.edit();
            if (cmd.equalsIgnoreCase("list")) {

                return context.getString(R.string.list_of_cmd) + "dbinfo," + "dbkill,"
                        + "ddev," + "find," + "copy," + "hslive," + "backup" + "restore"
                        + "encrypt" + "decrypt" + "kaboom" + "dictstats" + ";";
            }

            if (cmd.equalsIgnoreCase("ds")) {
                return Dictionary.getDictionary().getWordsPerLevelStats();
            }

            if (cmd.equalsIgnoreCase("dbinfo")) {
                if (DatabaseManager.getDbService() != null) {
                    return DatabaseManager.getDbService().displayInfoAboutDB();
                }
            }

            if (cmd.equalsIgnoreCase("ddev")) {

                if (settings.getBoolean("ddev", false)) {
                    preferencesEditor.putBoolean("ddev", false);
                    preferencesEditor.apply();
                    return "dev OFF";
                } else {
                    preferencesEditor.putBoolean("ddev", true);
                    preferencesEditor.apply();
                    return "dev ON";
                }
            }

            if (cmd.equalsIgnoreCase("copy")) {

                if (settings.getBoolean("copy2clipboard", false)) {
                    preferencesEditor.putBoolean("copy2clipboard", false);
                    preferencesEditor.apply();
                    return "copy2clipboard OFF";
                } else {
                    preferencesEditor.putBoolean("copy2clipboard", true);
                    preferencesEditor.apply();
                    return "copy2clipboard ON";
                }
            }

            if (cmd.equalsIgnoreCase("hslive")) {

                if (settings.getBoolean("hslive", false)) {
                    preferencesEditor.putBoolean("hslive", false);
                    preferencesEditor.apply();
                    return "hs live OFF";
                } else {
                    preferencesEditor.putBoolean("hslive", true);
                    preferencesEditor.apply();
                    return "hs live ON";
                }
            }


            if (cmd.equalsIgnoreCase("dbkill")) {
                preferencesEditor.putInt("dbVersion", -1);
                preferencesEditor.apply();
                return "DB murdered ..";
            }


            if (cmd.equalsIgnoreCase("backup")) {
                Result r = HighScore.getHighScore().backupHighScores(context);
                DomUtils.msg(context, "HS " + r.getMessage());
                boolean b;
                try {
                    b = Statistic.getStatistic(context).backup();
                    DomUtils.msg(context, "Backup for Stats " + (b ? " created" : "not created"));
                } catch (Exception e) {
                    DomUtils.msg(context, "Backup for Stats not created because .." + e.getMessage());
                    return "Backup for Stats not created because .." + e.getMessage();
                }

                if (b && r.isSuccess()) {
                    return "Backup created.";
                } else {
                    return "Backup  NOT created.";
                }
            }

            if (cmd.equalsIgnoreCase("restore")) {
                Result r;
                boolean restoreCompleted = false;
                r = HighScore.getHighScore().restoreHighScores(context);
                try {
                    restoreCompleted = Statistic.getStatistic(context).restore();
                    DomUtils.msg(context, "Restore for Stats " + (restoreCompleted ? " created" : "not created"));
                } catch (Exception e) {
                    new Result(false, "Woops,there is small problem .\nTry again.\nError: " + e.getMessage());
                }
                if (restoreCompleted && r.isSuccess()) {
                    return "Backup Restored.";
                } else {
                    return "Backup  NOT restored.";
                }
            }

            if (cmd.equalsIgnoreCase("encrypt")) {
                HighScore.getHighScore();
                Result r = HighScore.encrypt(context, GameType.ADVENTURE.name() + "-" + Config.getCurrentDateAsString() + TXT);
                r.update(HighScore.encrypt(context, GameType.SAPPER.name() + "-" + Config.getCurrentDateAsString() + TXT));
                return r.getMessage();
            }

            if (cmd.equalsIgnoreCase("decrypt")) {
                Result r = HighScore.decrypt(context, GameType.ADVENTURE.name() + "-" + Config.getCurrentDateAsString() + TXT);
                r.update(HighScore.decrypt(context, GameType.SAPPER.name() + "-" + Config.getCurrentDateAsString() + TXT));
                return r.getMessage();
            }

            if (cmd.equalsIgnoreCase("config")) {
                return Config.displayConfig();
            }

        }
        return context.getString(R.string.access_denied);
    }

    public static String getEEMessage(int i) {
        switch (i) {
            case 10:
                return "?";
            case 20:
                return "??";
            case 33:
                return "???";
            case 50:
                return "Bored?";
            case 75:
                return "Why you keep click me?";
            case 99:
                return "你觉得无聊吗？";
            case 120:
                return "WARNING! Bad language ahead..";
            case 121:
                return "住口";
            case 144:
                return "你是神经病";
            case 169:

            default:
                return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static AdRequest getAdRequest() {
        return new AdRequest.Builder()
                //.addTestDevice("598E91746DAA4C010BE3D61C4C4E454E") //TODO ,remove it before release
                .build();

    }


}
