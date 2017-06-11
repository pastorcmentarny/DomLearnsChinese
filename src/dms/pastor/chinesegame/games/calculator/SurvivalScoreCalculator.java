package dms.pastor.chinesegame.games.calculator;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.game.Game;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 17/10/2015
 */
public final class SurvivalScoreCalculator implements Calculator {

    public int calculate(Game game, long totalTime) {
        int finalBonus = getPlainBonus(game);
        double maxTimeForAnyBonus = 6 * Config.SECONDS;
        if (totalTime <= maxTimeForAnyBonus / 3) {
            return finalBonus;
        } else if (totalTime < maxTimeForAnyBonus) {
            Double multi = totalTime / maxTimeForAnyBonus;
            return finalBonus - Double.valueOf(multi * finalBonus).intValue();
        } else {
            double noPainTime = Config.ADVENTURE_NO_PENALTY_TIME * Config.SECONDS;
            Double painPoints = (totalTime - noPainTime) / 375;
            if (totalTime > 2 * noPainTime) {
                painPoints += (game.getLevel() / 5);
            }
            if (painPoints > game.getLevel()) {
                painPoints = (double) game.getLevel();
            }
            return (-1) * (painPoints.intValue());
        }

    }

    private int getPlainBonus(Game game) {
        int bonus = Config.DEFAULT_BONUS_POINTS;
        return bonus + Config.calcDictionarySizeBonus(Dictionary.getDictionary().getAllDictionarySize()) + game.getStage().getDifficulty() + Double.valueOf(game.getLevel() / 4).intValue();
    }

}
