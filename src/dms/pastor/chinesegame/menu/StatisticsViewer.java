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

        games = findViewById(R.id.games);
        adventureGames = findViewById(R.id.adventureGames);
        sapperGames = findViewById(R.id.sapperGames);
        totalTime = findViewById(R.id.totalTime);
        corrects = findViewById(R.id.corrects);
        wrongs = findViewById(R.id.wrongs);
        skipped = findViewById(R.id.skipped);
        maxCombo = findViewById(R.id.maxCombo);
        maxComboPoints = findViewById(R.id.maxComboPoints);
        maxScore = findViewById(R.id.maxScore);
        totalLevels = findViewById(R.id.totalLevels);
        highestLevelSapper = findViewById(R.id.highestLevelSapper);
        highestLevelAdventures = findViewById(R.id.highestLevelAdventures);
        totalTimeSapper = findViewById(R.id.totalTime_sapper);
        totalTimeAdventure = findViewById(R.id.totalTime_adventure);
        showPinyinUsed = findViewById(R.id.showPinyinUsed);
        healUsed = findViewById(R.id.healUsed);
        removeWrongAnswerUsed = findViewById(R.id.removeWrongAnswerUsed);
        cureUsed = findViewById(R.id.cureUsed);
        halfHalfHappen = findViewById(R.id.halfHalfHappen);
        poisonHappen = findViewById(R.id.poisonHappen);
        triple = findViewById(R.id.triple);
        jackpot = findViewById(R.id.jackpot);
        drainMana = findViewById(R.id.drainMana);
        minorPoints = findViewById(R.id.minorPoints);
        regeneration = findViewById(R.id.regeneration);
        shitHappens = findViewById(R.id.shitHappens);
        swapHpMp = findViewById(R.id.swapHpMp);
        frozen = findViewById(R.id.frozen);
        blind = findViewById(R.id.blind);
        showPinyin = findViewById(R.id.showPinyin);
        painKiller = findViewById(R.id.painKiller);
        sapperEasy = findViewById(R.id.sapper_easy);
        sapperHard = findViewById(R.id.sapper_hard);
        pinyinInsteadCharacter = findViewById(R.id.pinyin_instead_character);
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
