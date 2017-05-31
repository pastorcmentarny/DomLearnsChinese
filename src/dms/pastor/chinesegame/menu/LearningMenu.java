package dms.pastor.chinesegame.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.appbrain.AppBrain;
import com.google.android.gms.ads.AdView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.extras.LearningTipsActivity;
import dms.pastor.chinesegame.games.dictionarytest.DictionaryTestIntro;
import dms.pastor.chinesegame.games.hsk.basic.HskBasicIntro;
import dms.pastor.chinesegame.learning.chats.ChatIntro;
import dms.pastor.chinesegame.learning.dictionary.DictionaryList;
import dms.pastor.chinesegame.learning.dictionary.QuestionList;
import dms.pastor.chinesegame.learning.dictionary.SentenceList;
import dms.pastor.chinesegame.learning.lessons.LessonsIntro;
import dms.pastor.chinesegame.learning.sentencespattern.PatternIntro;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Utils;

/**
 * Author Dominik Symonowicz
 * Created 03/05/2015
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public final class LearningMenu extends Activity implements View.OnClickListener {
    private static final String TAG = "Learning Menu";
    private AlertDialog dialog;
    private Statistic statistic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.menu_learning);
        statistic = Statistic.getStatistic(this);

        loadAppBrain();

        loadAd();

        Button miniLessonButton = (Button) findViewById(R.id.lessonButton);
        miniLessonButton.setOnClickListener(this);
        Button learningTipsButton = (Button) findViewById(R.id.learningTipssButton);
        learningTipsButton.setOnClickListener(this);
        Button testsButton = (Button) findViewById(R.id.testsButton);
        testsButton.setOnClickListener(this);
        Button backToMainMenuButton = (Button) findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(this);
        Button dictButton = (Button) findViewById(R.id.dictButton);
        dictButton.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        //adView.pause();
        super.onPause();
        statistic.save();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        Intent ii;
        switch (v.getId()) {
            case R.id.lessonButton:
                lessonsDialog();
                break;
            case R.id.learningTipssButton:
                ii = new Intent(getApplicationContext(), LearningTipsActivity.class);
                startActivity(ii);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.testsButton:
                testsDialog();
                break;
            case R.id.dictButton:
                openDictionaryDialog();
                break;
            case R.id.backToMainMenuButton:
                finish();
                break;
        }
    }

    private void testsDialog() {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.learning_title).setItems(R.array.test_dialog, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii;
                switch (i) {
                    case 2:
                        Player.getPlayer();
                        ii = new Intent(getApplicationContext(), DictionaryTestIntro.class);
                        statistic.addGame();
                        startActivity(ii);
                        break;
                    case 1:
                        AlertDialog.Builder alertBox = new AlertDialog.Builder(LearningMenu.this);
                        alertBox.setTitle(getResources().getString(R.string.radical_title));
                        alertBox.setMessage(getResources().getString(R.string.radical_download));
                        alertBox.setPositiveButton(getString(R.string.go2www), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface di, final int arg) {
                                //noinspection SpellCheckingInspection
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dms.pastor.chineseradicalsgame")));

                            }
                        });
                        alertBox.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface di, final int arg) {
                                DomUtils.msg(getApplicationContext(), "Thank you anyway");

                            }
                        });
                        alertBox.show();
                        break;
                    case 0:
                        Player.getPlayer();
                        ii = new Intent(getApplicationContext(), HskBasicIntro.class);
                        statistic.addGame();
                        startActivity(ii);
                        break;
                    default:
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "openLearningDialog(" + i + ").");
                }
            }
        }).show();
    }

    private void lessonsDialog() {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.learning_title).setItems(R.array.lesson_dialog, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii;
                switch (i) {
                    case 0:
                        ii = new Intent(getApplicationContext(), LessonsIntro.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        break;
                    case 1:
                        ii = new Intent(getApplicationContext(), ChatIntro.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case 2:
                        ii = new Intent(getApplicationContext(), PatternIntro.class);
                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    default:
                        Log.w(TAG, getString(R.string.w_unknown_selection) + "lessonsDialog(" + i + ").");
                }
            }
        }).show();
    }

    private void openDictionaryDialog() {
        dialog = new AlertDialog.Builder(this).setTitle(R.string.dictionary_type).setItems(R.array.dict_type, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii;
                switch (i) {
                    case 0:
                        ii = new Intent(getApplicationContext(), DictionaryList.class);
                        break;
                    case 1:
                        ii = new Intent(getApplicationContext(), QuestionList.class);
                        break;
                    case 2:
                        ii = new Intent(getApplicationContext(), SentenceList.class);
                        break;
                    default:
                        ii = new Intent(getApplicationContext(), DictionaryList.class);
                        break;
                }
                startActivity(ii);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        }).show();
    }

    private void loadAppBrain() {
        try {
            AppBrain.init(this);
        } catch (Exception e) {
            Log.d(getString(R.string.ab_e), getString(R.string.ab_e_init) + e.getMessage());
        }
    }

    private void loadAd() {
        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            com.google.android.gms.ads.AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }
    }

}
