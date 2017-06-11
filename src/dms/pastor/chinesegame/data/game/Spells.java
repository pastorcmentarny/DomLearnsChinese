package dms.pastor.chinesegame.data.game;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.data.game.Player.getPlayer;
import static dms.pastor.chinesegame.utils.UIUtils.setInvisibleButton;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 21/11/2012
 */
public final class Spells {
    private static final String TAG = "Spells/Events";
    private static final Player PLAYER = getPlayer();
    private static Context context;
    private static Activity activity;

    public Spells(Activity activity, Context context) {
        Spells.activity = activity;
        Spells.context = context;
    }

    public static void castCure(TextView status) {
        getPlayer().removeNegativeSpells();
        status.setText(R.string.cured);
        UIUtils.setTextColor(status, R.color.good_news, activity);
    }

    public static void eventHappen(String message, boolean isGoodEvent) {
        DomUtils.displayWandToast(context, activity, message, false, isGoodEvent);
    }

    public static Result castSpellShowPinyin(TextView currentPinyin, Button pinyinButton) {
        if (PLAYER.getMana() >= Config.PINYIN_SPELL_COST) {
            PLAYER.setMana(PLAYER.getMana() - Config.PINYIN_SPELL_COST);
            currentPinyin.setVisibility(TextView.VISIBLE);
            pinyinButton.setEnabled(false);
            return new Result(true, "pinyin is shown");
        } else {
            return new Result(false, context.getResources().getString(R.string.no_mana));
        }
    }

    public static void blind(Button answer1Button, Button answer2Button, Button answer3Button, Button answer4Button) {
        setInvisibleButton(activity, answer1Button);
        setInvisibleButton(activity, answer2Button);
        setInvisibleButton(activity, answer3Button);
        setInvisibleButton(activity, answer4Button);
    }

    public static Result castShowPinyinHideCharacter(TextView currentCharacter, TextView currentPinyin, Button pinyinButton) {
        currentPinyin.setVisibility(TextView.VISIBLE);
        currentCharacter.setVisibility(TextView.INVISIBLE);
        pinyinButton.setEnabled(false);
        return new Result(true, "guess pinyin instead of character");
    }

    public static void doubleHP() {
        Log.i(TAG, "EVENT: doubling HP");
        PLAYER.setHealth(PLAYER.getHealth() * 2);
    }

    public static void halfHP() {
        Log.i(TAG, "EVENT: half HP");
        PLAYER.setHealth(PLAYER.getHealth() / 2);
    }

    public static void doubleMP() {
        Log.i(TAG, "EVENT: doubling mana");
        PLAYER.setMana(PLAYER.getMana() * 2);
    }

    public static void halfMP() {
        Log.i(TAG, "EVENT: half mana");
        PLAYER.setMana(PLAYER.getMana() / 2);
    }

    public void castRegeneration() {
        PLAYER.activateHealthRegeneration(new Random().nextInt(Config.TURN_RANGE));
        PLAYER.activateManaRegeneration(new Random().nextInt(Config.TURN_RANGE));
    }

    public void castSwap() {
        int mana = PLAYER.getHealth();
        int health = PLAYER.getMana();
        PLAYER.setHealth(health);
        PLAYER.setMana(mana);
    }

    public void castManaDrain() {
        PLAYER.setMana(0);

    }

    public int castMinorPointsBonus() {
        int bonus = new Random().nextInt((PLAYER.game.getLevel() + 1));
        PLAYER.addBonus();

        bonus += (PLAYER.getHealth() + PLAYER.getMana() + new Random().nextInt(4));
        PLAYER.setScore(PLAYER.getScore() + bonus);
        return bonus;
    }

    public void castTriple() {
        PLAYER.activateTripleBonus(new Random().nextInt(4));
    }

    public void castPoison() {
        PLAYER.activatePoison(new Random().nextInt(4));
    }

    public void castShitHappens() {
        int halfHealth = PLAYER.getHealth();
        halfHealth -= halfHealth / 2;
        PLAYER.setHealth(halfHealth);

        PLAYER.setHpRegeneration(false);
        PLAYER.setManaRegeneration(false);
        PLAYER.setTripleBonusToFalse();

        PLAYER.setHpRegenerationTurnLeft(0);
        PLAYER.setManaRegenerationTurnLeft(0);
        PLAYER.setTripleBonusTurnLeft();

        castManaDrain();
        castPoison();
    }

    public void castHeal(TextView status, Activity activity) {

        if (PLAYER.getMana() >= Config.HEAL_SPELL_COST) {
            PLAYER.setMana(PLAYER.getMana() - Config.HEAL_SPELL_COST);
            PLAYER.setHealth(PLAYER.getHealth() + Config.HEAL_HP_VALUE);
            status.setText(String.format(Locale.ENGLISH, "Heal by %d", Config.HEAL_HP_VALUE));
            UIUtils.setTextColor(status, R.color.good_news, activity);
        } else {
            status.setText(context.getResources().getString(R.string.no_mana));
            UIUtils.setTextColor(status, R.color.bad_news, activity);
        }

    }
}
