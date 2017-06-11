package dms.pastor.chinesegame.games.calculator;

import java.math.BigDecimal;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.data.game.Game;

import static dms.pastor.chinesegame.Config.DEFAULT_BONUS_POINTS;
import static dms.pastor.chinesegame.Config.SECONDS;
import static dms.pastor.chinesegame.Config.calcDictionarySizeBonus;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 17/10/2015
 */
public final class SapperScoreCalculator implements Calculator {
    private static final int NEGATIVE = -1;

    public int calculate(Game game, long totalTime) {

        int finalBonus = getPlainBonus(game);
        long time = (int) (Config.SAPPER_NO_PENALTY_TIME * SECONDS - totalTime); //replace with  currentTime


        if (time > 6 * SECONDS) {
            finalBonus += ((time - 6750) / 250);
        } else if (time > 1500) {
            Long timex = (long) (Config.SAPPER_NO_PENALTY_TIME * SECONDS);
            BigDecimal resultD = new BigDecimal(time).divide(new BigDecimal(timex), BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultM = new BigDecimal(finalBonus).multiply(resultD);
            finalBonus = resultM.intValue();
        } else {
            long pain = (4 - (time / 400)) * (game.getLevel() / 10) * NEGATIVE;
            finalBonus = Long.valueOf(pain).intValue();
        }
        if (finalBonus < NEGATIVE * game.getLevel()) {
            finalBonus = NEGATIVE * game.getLevel();
        }
        return finalBonus;
    }

    //TODO move to Config
    private static int getPlainBonus(Game game) {
        return (int) ((DEFAULT_BONUS_POINTS + calcDictionarySizeBonus(game.getGameWordsList().size()) * game.getStage().getScoreBonusMultiply()) + (game.getLevel() * 3 / 5));
    }

}
