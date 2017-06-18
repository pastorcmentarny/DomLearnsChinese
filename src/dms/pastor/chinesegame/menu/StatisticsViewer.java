package dms.pastor.chinesegame.menu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.utils.DomUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 05/03/2014
 */
public final class StatisticsViewer extends Activity {
    private TextView games;
    private TextView adventureGames;
    private TextView sapperGames;
    private TextView totalTime;
    private TextView corrects;
    private TextView wrongs;
    private TextView skipped;
    private TextView maxCombo;
    private TextView maxComboPoints;
    private TextView maxScore;
    private TextView totalLevels;
    private TextView highestLevelSapper;
    private TextView highestLevelAdventures;
    private TextView totalTimeSapper;
    private TextView totalTimeAdventure;
    private TextView showPinyinUsed;
    private TextView healUsed;
    private TextView removeWrongAnswerUsed;
    private TextView cureUsed;
    private TextView halfHalfHappen;
    private TextView poisonHappen;
    private TextView triple;
    private TextView jackpot;
    private TextView drainMana;
    private TextView minorPoints;
    private TextView regeneration;
    private TextView shitHappens;
    private TextView swapHpMp;
    private TextView frozen;
    private TextView blind;
    private TextView showPinyin;
    private TextView painKiller;
    private TextView sapperEasy;
    private TextView pinyinInsteadCharacter;
    private TextView sapperHard;
    private Statistic stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistic);
        stats = Statistic.getStatistic(this);

        games = (TextView) findViewById(R.id.games);
        adventureGames = (TextView) findViewById(R.id.adventureGames);
        sapperGames = (TextView) findViewById(R.id.sapperGames);
        totalTime = (TextView) findViewById(R.id.totalTime);
        corrects = (TextView) findViewById(R.id.corrects);
        wrongs = (TextView) findViewById(R.id.wrongs);
        skipped = (TextView) findViewById(R.id.skipped);
        maxCombo = (TextView) findViewById(R.id.maxCombo);
        maxComboPoints = (TextView) findViewById(R.id.maxComboPoints);
        maxScore = (TextView) findViewById(R.id.maxScore);
        totalLevels = (TextView) findViewById(R.id.totalLevels);
        highestLevelSapper = (TextView) findViewById(R.id.highestLevelSapper);
        highestLevelAdventures = (TextView) findViewById(R.id.highestLevelAdventures);
        totalTimeSapper = (TextView) findViewById(R.id.totalTime_sapper);
        totalTimeAdventure = (TextView) findViewById(R.id.totalTime_adventure);
        showPinyinUsed = (TextView) findViewById(R.id.showPinyinUsed);
        healUsed = (TextView) findViewById(R.id.healUsed);
        removeWrongAnswerUsed = (TextView) findViewById(R.id.removeWrongAnswerUsed);
        cureUsed = (TextView) findViewById(R.id.cureUsed);
        halfHalfHappen = (TextView) findViewById(R.id.halfHalfHappen);
        poisonHappen = (TextView) findViewById(R.id.poisonHappen);
        triple = (TextView) findViewById(R.id.triple);
        jackpot = (TextView) findViewById(R.id.jackpot);
        drainMana = (TextView) findViewById(R.id.drainMana);
        minorPoints = (TextView) findViewById(R.id.minorPoints);
        regeneration = (TextView) findViewById(R.id.regeneration);
        shitHappens = (TextView) findViewById(R.id.shitHappens);
        swapHpMp = (TextView) findViewById(R.id.swapHpMp);
        frozen = (TextView) findViewById(R.id.frozen);
        blind = (TextView) findViewById(R.id.blind);
        showPinyin = (TextView) findViewById(R.id.showPinyin);
        painKiller = (TextView) findViewById(R.id.painKiller);
        sapperEasy = (TextView) findViewById(R.id.sapper_easy);
        sapperHard = (TextView) findViewById(R.id.sapper_hard);
        pinyinInsteadCharacter = (TextView) findViewById(R.id.pinyin_instead_character);
        setupUI();
    }

    private void setupUI() {
        games.setText(String.valueOf(stats.getGames()));
        adventureGames.setText(String.valueOf(stats.getAdventureGames()));
        sapperGames.setText(String.valueOf(stats.getSapperGames()));
        totalTime.setText(DomUtils.getResultTimeAsString(stats.getTotalTime()));
        corrects.setText(String.valueOf(stats.getCorrects()));
        wrongs.setText(String.valueOf(stats.getWrongs()));
        skipped.setText(String.valueOf(stats.getSkipped()));
        maxCombo.setText(String.valueOf(stats.getMaxCombo()));
        maxComboPoints.setText(String.valueOf(stats.getMaxComboPoints()));
        maxScore.setText(String.valueOf(stats.getMaxScore()));
        totalLevels.setText(String.valueOf(stats.getTotalLevels()));
        highestLevelSapper.setText(String.valueOf(stats.getHighestLevelAdventures()));
        highestLevelAdventures.setText(String.valueOf(stats.getHighestLevelSapper()));
        totalTimeSapper.setText(DomUtils.getResultTimeAsString(stats.getTotalTimeSapper()));
        totalTimeAdventure.setText(DomUtils.getResultTimeAsString(stats.getTotalTimeAdventure()));
        showPinyinUsed.setText(String.valueOf(stats.getShowPinyinUsed()));
        healUsed.setText(String.valueOf(stats.getHealUsed()));
        removeWrongAnswerUsed.setText(String.valueOf(stats.getRemoveWrongAnswerUsed()));
        cureUsed.setText(String.valueOf(stats.getCureUsed()));
        halfHalfHappen.setText(String.valueOf(stats.getHalfHalfHappen()));
        poisonHappen.setText(String.valueOf(stats.getPoisonHappen()));
        triple.setText(String.valueOf(stats.getTriple()));
        jackpot.setText(String.valueOf(stats.getJackpot()));
        drainMana.setText(String.valueOf(stats.getDrainMana()));
        minorPoints.setText(String.valueOf(stats.getMinorPoints()));
        regeneration.setText(String.valueOf(stats.getRegeneration()));
        shitHappens.setText(String.valueOf(stats.getShitHappens()));
        swapHpMp.setText(String.valueOf(stats.getSwapHpMp()));
        frozen.setText(String.valueOf(stats.getFrozen()));
        blind.setText(String.valueOf(stats.getBlind()));
        showPinyin.setText(String.valueOf(stats.getShowPinyin()));
        painKiller.setText(String.valueOf(stats.getPainKiller()));
        sapperEasy.setText(String.valueOf(stats.getSapperDiffEasy()));
        sapperHard.setText(String.valueOf(stats.getSapperDiffHard()));
        pinyinInsteadCharacter.setText(String.valueOf(stats.getShowPinyinHideCharacter()));
    }


}
