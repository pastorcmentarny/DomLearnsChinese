package dms.pastor.chinesegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Game;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.data.game.score.HighScoreList;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.db.DictionaryHandler;
import dms.pastor.chinesegame.extras.*;
import dms.pastor.chinesegame.games.survival.SurvivalIntro;
import dms.pastor.chinesegame.games.survival.saper.SapperGame;
import dms.pastor.chinesegame.games.survival.saper.SapperIntro;
import dms.pastor.chinesegame.games.survival.word.WordSurvival;
import dms.pastor.chinesegame.menu.*;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.Config.APP_NAME;
import static dms.pastor.chinesegame.data.game.score.HighScore.getNewHighScore;
import static dms.pastor.chinesegame.utils.DomUtils.displayError;
import static dms.pastor.chinesegame.utils.UIUtils.usernameDialog;
import static dms.pastor.chinesegame.utils.Utils.getEEMessage;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 */
public final class AppLauncher extends Activity implements View.OnClickListener, Runnable {
    private static final String TAG = "App launcher activity";
    private static final String TOPIC = "TOPIC";
    private static Handler handler;
    private Player player;
    private DatabaseManager dbManager;
    private SharedPreferences preferences;
    private SharedPreferences settings;
    private Thread dictionaryLoaderThread;
    private ProgressDialog progressDialog;
    private Button highScoreButton;
    private AlertDialog dialog;
    private HighScore highScore;
    private Statistic statistic;

    public AppLauncher() {
        handler = new DictionaryHandler(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        player = Player.getPlayer(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(AppLauncher.this);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        UIUtils.loadAd(this, this);

        TextView mainTitle = (TextView) findViewById(R.id.gameTitle);
        mainTitle.setOnClickListener(this);
        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);
        highScoreButton = (Button) findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(this);
        Button optionsButton = (Button) findViewById(R.id.settingsButton);
        optionsButton.setOnClickListener(this);
        Button extrasButton = (Button) findViewById(R.id.extras_button);
        extrasButton.setOnClickListener(this);
        Button showLearningButton = (Button) findViewById(R.id.showLearningButton);
        showLearningButton.setOnClickListener(this);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText(DomUtils.getAppVersion(this));

        setup();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*
    FIX INFO:
    onPause is added to solve error appearing in logs (it did not crash app .... suprisely
        Error:
        11-29 23:24:09.162: ERROR/WindowManager(5662):
        Activity dms.pastor.chinesegame.AppLauncher has leaked window
         com.android.internal.policy.impl.PhoneWindow$DecorView@410e4fc8 that was originally added here
         android.view.WindowLeaked: Activity dms.pastor.chinesegame.AppLauncher has leaked window
         com.android.internal.policy.impl.PhoneWindow$DecorView@410e4fc8 that was originally added here
*/
    @Override
    public void onPause() {
        super.onPause();
        statistic.save();
        if (dialog != null) {
            dialog.dismiss();
        }
        try {
            if (dictionaryLoaderThread != null) {
                dictionaryLoaderThread.interrupt();
                dictionaryLoaderThread = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to kill DataLoaderThread");
        }
    }

/* /TODO remove it in 18.1
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
*/

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    public Thread getDictionaryLoaderThread() {
        return dictionaryLoaderThread;
    }

    private void setup() {
        statistic = Statistic.getStatistic(this);
        loadHighScores();
        loadData();
        if (preferences.getBoolean("first", true)) {
            usernameDialog(this);
            Editor preferencesEditor = preferences.edit();
            preferencesEditor.putBoolean("first", false);
            preferencesEditor.apply();
        }
    }

    @Override
    public final void onClick(final View view) {
        switch (view.getId()) {
            case R.id.newGameButton:
                newGameDialog();
                break;
            case R.id.highScoreButton:
                openHighScoreSelectionDialog();
                break;
            case R.id.showLearningButton:
                if (Dictionary.getDictionary().isDBOk()) {
                    Intent ii = new Intent(getApplicationContext(), LearningMenu.class);
                    startActivity(ii);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    displayError(getApplicationContext(), player.getDbErrorMessage());
                }
                break;
            case R.id.settingsButton:
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
                break;
            case R.id.extras_button:
                openExtrasDialog();
                break;
            case R.id.gameTitle:
                String msg = getEEMessage(player.getEECounter());
                if (msg != null) {
                    DomUtils.msg(this, msg);
                }
                break;
            default:
                Log.w(TAG, getString(R.string.w_unknown_selection));
        }
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.aboutGameButton:
                openAboutDialog(this);
                return true;
            case R.id.stats_menu:
                Intent ii = new Intent(getApplicationContext(), StatisticsViewer.class);
                startActivity(ii);
                return true;
            case R.id.rateMeButton:
                showRateMeDialog();
                return true;
            default:
                Log.w(TAG, "Unknown choice selected for item id:" + item.getItemId());
                return false;
        }
    }

    public final boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    private void showRateMeDialog() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(getResources().getString(R.string.rateMe_title));
        alertBox.setMessage(getResources().getString(R.string.rateMe_about));
        alertBox.setPositiveButton(getString(R.string.rate_it), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_NAME)));
            }
        });
        alertBox.setNeutralButton(getString(R.string.send_email), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", Config.MY_EMAIL, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + APP_NAME);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });
        alertBox.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                DomUtils.msg(getApplicationContext(), "Thank you anyway");
            }
        });
        alertBox.show();
    }

    private void openHighScoreSelectionDialog() {

        dialog = new AlertDialog.Builder(this).setTitle(R.string.highscore_button_title).setItems(R.array.hs, new DialogInterface.OnClickListener() {
            @SuppressWarnings("VariableNotUsedInsideIf")
            public void onClick(final DialogInterface dialoginterface, final int i) {

                switch (i) {
                    case 0:
                        if (highScore != null) {
                            Intent ii = new Intent(getApplicationContext(), HighScoreList.class);
                            ii.putExtra("HS", "ADVENTURE");
                            startActivity(ii);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.highscore_no_available), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if (highScore != null) {
                            Intent ii = new Intent(getApplicationContext(), HighScoreList.class);
                            ii.putExtra("HS", "SAPPER");
                            startActivity(ii);
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.highscore_no_available), Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "openLearningDialog(" + i + ").");
                }
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishing()) {
            Log.i(TAG, "Goodbye application! Goodbye weird entity who read this log.");
        }
    }

    @Override
    public void run() {
        if (dbManager == null) {
            dbManager = DatabaseManager.getDbManager(this);
        }
        handler.sendEmptyMessage(0);
    }

    private void newGameDialog() {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.new_game_title).setItems(R.array.newGame, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                switch (i) {
                    case 0:
                        startAdventureGame();
                        break;
                    case 1:
                        startSaperGame();
                        break;
                    default:
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "newGameDialog(" + i + ").");
                }
            }
        }).show();
    }

    private void startSaperGame() {
        Intent ii;
        if (settings.getBoolean("showIntro", true)) {
            ii = new Intent(getApplicationContext(), SapperIntro.class);
        } else {
            player.restart(GameType.SAPPER);
            statistic.addSapperGame();
            ii = new Intent(getApplicationContext(), SapperGame.class);
            Dictionary dictionary = Dictionary.getDictionary();
            dictionary.readDictionaryFromFile(getApplicationContext(), R.raw.dictionary, null);
            player.getGame().setGameWordList(dictionary.getWordsForLevel(1));
        }
        startActivity(ii);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void startAdventureGame() {
        Intent ii;
        if (settings.getBoolean("showIntro", true)) {
            ii = new Intent(getApplicationContext(), SurvivalIntro.class);
            ii.putExtra("GAME", "ADVENTURE");
        } else {
            ii = new Intent(getApplicationContext(), WordSurvival.class);
            player.restart(GameType.ADVENTURE);
            player.getGame() = new Game(getApplicationContext(), GameType.ADVENTURE);
            statistic.addAdventureGame();
            Dictionary dictionary = Dictionary.getDictionary();
            Result result = dictionary.readDictionaryFromFile(getApplicationContext(), R.raw.dictionary, null);
            if (result.isFail()) {
                Log.w(TAG, result.getMessage());
            }
            player.getGame().setGameWordList(dictionary.getWordsForLevel(1));
            player.getGame().timeStart();
        }
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void openExtrasDialog() {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.extras_title).setItems(R.array.extras, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii;
                switch (i) {
                    case 0:
                        ii = new Intent(getApplicationContext(), ToDo4Go.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case 1:
                        ii = new Intent(getApplicationContext(), UsefulContactDetails.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case 2:
                        ii = new Intent(getApplicationContext(), CultureInfoActivity.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case 3:
                        ii = new Intent(getApplicationContext(), LinksActivity.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case 4:
                        if (statistic != null) {
                            ii = new Intent(getApplicationContext(), WordMistakesCounterView.class);
                            startActivity(ii);
                        } else {
                            Toast.makeText(getApplicationContext(), "You can't see list of words that you didn't guess due problem with Statistics.Sorry", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        ii = new Intent(getApplicationContext(), RandomWordActivity.class);
                        startActivity(ii);
                        break;
                    default:
                        DomUtils.msg(getApplicationContext(), "Work in progress");
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "openExtrasDialog(" + i + ").");
                        break;
                }
            }
        }).show();
    }

    private void openAboutDialog(final Context context) {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.about_title).setItems(R.array.runAbout, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii = new Intent(context, About.class);
                switch (i) {
                    case 0:
                        ii.putExtra(TOPIC, "ME");
                        startActivity(ii);
                        break;
                    case 1:
                        ii.putExtra(TOPIC, "PROGRAM");
                        startActivity(ii);
                        break;
                    case 2:
                        ii.putExtra(TOPIC, "THANKS");
                        startActivity(ii);
                        break;
                    case 3:
                        ii.putExtra(TOPIC, "EULA");
                        startActivity(ii);
                        break;
                    default:
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "openAboutDialog(" + i + ").");
                        break;
                }
            }
        }).show();
    }

    private void loadData() {
        if (DatabaseManager.isNotOK() || DatabaseManager.isUpdateNeeded(this)) {
            loadDictionary();
            Result r = DatabaseManager.setUpdated(this);
            Log.i(TAG, r.getMessage());
        }
    }

    private void loadHighScores() {
        Log.i(TAG, "Loading high scores..");
        if (highScore != null) {
            highScoreButton.setEnabled(highScore.isAvailable());
        } else {
            highScore = getNewHighScore(this);
            if (highScore != null) {
                highScoreButton.setEnabled(highScore.isAvailable());
            } else {
                highScoreButton.setEnabled(false);
            }
        }
    }

    private void loadDictionary() {
        if (dictionaryLoaderThread == null) {
            progressDialog = ProgressDialog.show(this, "Loading....", "Dictionary", true, false);
            dictionaryLoaderThread = new Thread(this);
            try {
                dictionaryLoaderThread.start();
            } catch (IllegalThreadStateException itse) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.woops) + "\nThread has been started already.\nMsg:" + itse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
