package dms.pastor.chinesegame.games.calculator;

import java.math.BigDecimal;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.data.game.Game;

import static dms.pastor.chinesegame.Config.SECONDS;

/**
 * Author Dominik Symonowicz
 * Created 2015-10-17
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
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

    private int getPlainBonus(Game game) {
        int bonus = Config.DEFAULT_BONUS_POINTS;
        return (int) ((bonus + Config.calcDictionarySizeBonus(game.getGameWordsList().size()) * game.getStage().getScoreBonusMultiply()) + (game.getLevel() * 3 / 5));
    }

}
