package dms.pastor.chinesegame.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.db.DatabaseManager;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;
import dms.pastor.chinesegame.utils.UIUtils;
import dms.pastor.chinesegame.utils.Utils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 24/11/2012
 */
public final class Options extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "OPTIONS";
    private CheckBox playSound;
    private CheckBox vibrate;
    private CheckBox showIntro, polishMode;
    private SharedPreferences settings;
    private HighScore highScore;
    private Statistic statistic;
    private TextView usernameValue;
    private CheckBox sapperPinyin;

    // performs specific task on each interval of time
    private final Runnable timerTicker = new Runnable() {
        public void run() {
            updateUI();
        }
    };
    private Timer myTimer;

    private void runTaskAfterTimeout() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, 0, Config.REFRESH);
    }

    private void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.options);
        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        highScore = HighScore.getHighScore();
        statistic = Statistic.getStatistic(this);
        CheckBox fixAll = (CheckBox) findViewById(R.id.fixAll);
        fixAll.setOnClickListener(this);
        playSound = (CheckBox) findViewById(R.id.play_sound);
        playSound.setOnClickListener(this);
        vibrate = (CheckBox) findViewById(R.id.vibrate);
        vibrate.setOnClickListener(this);
        Button changeNameButton = (Button) findViewById(R.id.changeNameButton);
        changeNameButton.setOnClickListener(this);
        showIntro = (CheckBox) findViewById(R.id.showIntro);
        showIntro.setOnClickListener(this);
        usernameValue = (TextView) findViewById(R.id.username_value);
        usernameValue.setOnClickListener(this);
        sapperPinyin = (CheckBox) findViewById(R.id.sapper_pinyin);
        sapperPinyin.setOnClickListener(this);
        polishMode = (CheckBox) findViewById(R.id.polishMode);
        polishMode.setOnClickListener(this);

        Button backup = (Button) findViewById(R.id.backup);
        backup.setOnClickListener(this);
        Button restore = (Button) findViewById(R.id.restore);
        restore.setOnClickListener(this);
        loadAd();
        updateUI();
    }

    protected void onPause() {
        super.onPause();
        if (myTimer != null) {
            myTimer.cancel();
        }
    }

    public void onResume() {
        super.onResume();
        runTaskAfterTimeout();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor preferencesEditor = settings.edit();
        Result result;
        switch (v.getId()) {
            case R.id.changeNameButton:
                UIUtils.usernameDialog(this);
                break;
            case R.id.fixAll:
                displayFixAllDialog();
                return;
            case R.id.backup:
                result = backup();
                if (result.isSuccess()) {
                    DomUtils.msg(this, "Backup done.");
                } else {
                    DomUtils.msg(this, result.getMessage());
                }
                return;
            case R.id.restore:
                result = restore();
                if (result.isSuccess()) {
                    DomUtils.msg(this, "Backup restored.");
                } else {
                    DomUtils.msg(this, result.getMessage());
                }
                return;
            case R.id.polishMode:
                preferencesEditor.putBoolean("polishMode", polishMode.isChecked());
                break;
            case R.id.showIntro:
                preferencesEditor.putBoolean("showIntro", showIntro.isChecked());
                break;
            case R.id.vibrate:
                preferencesEditor.putBoolean("vibrate", vibrate.isChecked());
                break;
            case R.id.play_sound:
                preferencesEditor.putBoolean("playSound", playSound.isChecked());
                break;
            case R.id.sapper_pinyin:
                preferencesEditor.putBoolean("sapperPinyin", sapperPinyin.isChecked());
                break;
            default:
                break;
        }
        boolean r = preferencesEditor.commit();
        if (r) {
            makeText(this, getString(R.string.saved), LENGTH_SHORT).show();
        } else {
            makeText(this, getString(R.string.unable_to_save), LENGTH_SHORT).show();
        }
        updateUI();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("kolor", parent.getItemAtPosition(position).toString().substring(0, 3).toUpperCase());
        prefsEditor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, getString(R.string.nothing_selected));
    }

    private void displayFixAllDialog() {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("WARNING");
        dialog.setMessage("It will clean all statistics, high score and so.Are you sure?");
        dialog.setPositiveButton("Fix All", getFixAllOnClickListener());
        dialog.setNegativeButton(getResources().getString(R.string.cancel), getCancelForFixAllOnClickListener());
        dialog.show();
    }

    @NonNull
    private static DialogInterface.OnClickListener getCancelForFixAllOnClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                Log.d(TAG, "User cancelled fix all");
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener getFixAllOnClickListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                Result result = fixALL();
                if (result.isSuccess()) {
                    DomUtils.msg(getApplicationContext(), "Problem fixed.");
                } else {
                    DomUtils.msg(getApplicationContext(), result.getMessage());
                }
            }
        };
    }

    private Result backup() {
        Result r;
        r = highScore.backupHighScores(this);
        try {
            boolean backupCompleted = statistic.backup();
            r.update(new Result(backupCompleted, backupCompleted ? "Backup saved." : "Backup was not saved."));
        } catch (Exception e) {
            new Result(false, "Woops,there is small problem .\nTry again.\nError: " + e.getMessage());
        }
        return r;
    }

    private Result restore() {
        Result r;
        r = highScore.restoreHighScores(this);
        try {
            boolean backupCompleted = statistic.restore();
            r.update(new Result(backupCompleted, backupCompleted ? "Restore saved." : "Backup was not restored."));
        } catch (Exception e) {
            new Result(false, "Woops,there is small problem .\nTry again.\nError: " + e.getMessage());
        }
        return r;
    }

    private Result fixALL() {
        Result r;
        r = DatabaseManager.reloadAll(getApplicationContext());
        HighScore hs = HighScore.getHighScore();
        r.update(hs.fixHighScores());
        boolean rs = statistic.reset();
        r.update(new Result(rs, rs ? "Stats fixed" : "Stats not fixed"));
        return r;
    }

    private void loadAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }

    private void updateUI() {
        playSound.setChecked(settings.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND));
        vibrate.setChecked(settings.getBoolean("vibrate", Config.DEFAULT_VIBRATE));
        usernameValue.setText(settings.getString("username", Config.DEFAULT_USER_NAME));
        showIntro.setChecked(settings.getBoolean("showIntro", Config.DEFAULT_INTRO));
        sapperPinyin.setChecked(settings.getBoolean("sapperPinyin", Config.DEFAULT_SHOW_PINYIN_SAPPER));
        polishMode.setChecked(settings.getBoolean("polishMode", Config.DEFAULT_SHOW_POLISH));
    }
}
