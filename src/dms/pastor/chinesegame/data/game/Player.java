package dms.pastor.chinesegame.data.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;
import java.util.Random;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.enums.BonusType;
import dms.pastor.chinesegame.common.enums.GameType;
import dms.pastor.chinesegame.data.learning.chats.Chat;
import dms.pastor.chinesegame.data.learning.lessons.Lesson;
import dms.pastor.chinesegame.data.learning.lessons.MiniLessons;
import dms.pastor.chinesegame.data.learning.patterns.Pattern;

public
/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 07/11/2012
 */
final class Player {
    private static final String TAG = "PLAYER";
    private static Player player;
    private final Context context;
    public Game game;
    public MiniLessons miniLessons = null;
    private String dbErrorMessage = "";
    private boolean poisoned = false;
    private boolean hpRegeneration = false;
    private boolean manaRegeneration = false;
    private boolean tripleBonus = false;
    private boolean cold = false;


    private int score;
    private int health;
    private int mana;

    private int combo = 0;
    private int poisonedTurnLeft = 0;
    private int hpRegenerationTurnLeft = 0;
    private int manaRegenerationTurnLeft = 0;
    private int tripleBonusTurnLeft = 0;

    private String selectedLesson = null;
    private Pattern currentPattern;
    private Chat currentChat;
    private int eeCounter = 0;

    private Player(Context context) {
        this.context = context;
        restart(GameType.NONE);
    }

    public static synchronized Player getPlayer(Context context) {

        if (player == null) {
            player = new Player(context);
        }
        return player;
    }

    public static synchronized Player getPlayer() {
        return player;
    }

    public int getEECounter() {
        if (player != null) {
            eeCounter++;
            return eeCounter;
        } else {
            return 0;
        }

    }

    private boolean isPoisoned() {
        return poisoned;
    }

    void setHpRegeneration(boolean hpRegeneration) {
        this.hpRegeneration = hpRegeneration;
    }

    void setManaRegeneration(boolean manaRegeneration) {
        this.manaRegeneration = manaRegeneration;
    }

    void setHpRegenerationTurnLeft(int hpRegenerationTurnLeft) {
        this.hpRegenerationTurnLeft = hpRegenerationTurnLeft;
    }

    void setManaRegenerationTurnLeft(int manaRegenerationTurnLeft) {
        this.manaRegenerationTurnLeft = manaRegenerationTurnLeft;
    }

    public String nextTurn() {
        game.nextTurn();
        StringBuilder status = new StringBuilder("");
        if (hpRegeneration) {
            setHealth(getHealth() + Config.REGEN_HP_VALUE);
            hpRegenerationTurnLeft--;
            status.append("HP regen by " + Config.REGEN_HP_VALUE + "\n");
            if (hpRegenerationTurnLeft <= 0) {
                hpRegeneration = false;
            }
        }

        if (manaRegeneration) {
            setMana(getMana() + Config.REGEN_MANA_VALUE);
            manaRegenerationTurnLeft--;
            status.append("Mana regen by " + Config.REGEN_HP_VALUE + "\n");
            if (manaRegenerationTurnLeft <= 0) {
                manaRegeneration = false;

            }
        }

        if (isPoisoned()) {
            setHealth(getHealth() - Config.POISON_VALUE);
            poisonedTurnLeft--;
            status.append("Poison caused " + Config.REGEN_HP_VALUE + "dmg.\n");
            if (poisonedTurnLeft <= 0) {
                poisoned = false;
            }
        }
        if (tripleBonus) {
            tripleBonusTurnLeft--;
            if (tripleBonusTurnLeft <= 0) {
                tripleBonus = false;
            }
        }
        return status.toString();
    }

    public void restart(GameType gameType) {
        this.score = 0;
        this.health = 100;
        this.mana = 100;
        this.hpRegeneration = false;
        this.manaRegeneration = false;
        this.poisoned = false;
        this.poisonedTurnLeft = 0;
        this.hpRegenerationTurnLeft = 0;
        this.manaRegenerationTurnLeft = 0;
        this.combo = 0;
        this.tripleBonus = false;
        this.tripleBonusTurnLeft = 0;
        this.cold = false;
        game = new Game(context, gameType);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        Log.i(TAG, "adding score:" + score);
        this.score += score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public boolean isAlive() {
        return health > 0;
    }


    public boolean isDead() {
        return !isAlive();
    }


    public boolean isCold() {
        return cold;
    }

    public void setCold(boolean cold) {
        this.cold = cold;
    }

    public void addCombo() {
        combo += 1;
    }

    public int claimComboBonus(int level) {
        int bonus = getCurrentComboBonus(level);
        combo = 0;
        return bonus;
    }

    public int getCurrentComboBonus(int level) {
        int bonus = Config.BONUS_POINTS_BASE;
        if (combo >= Config.COMBO_MINIMUM) {
            for (int i = 1; i <= combo; i++) {
                bonus += Config.BONUS_POINTS_BASE + i;
            }
            bonus += level;
        }
        return bonus;
    }

    void activateHealthRegeneration(int turns) {
        setHpRegeneration(true);
        setHpRegenerationTurnLeft(turns);
    }

    void activateManaRegeneration(int turns) {
        setManaRegeneration(true);
        setManaRegenerationTurnLeft(turns);
    }

    void activatePoison(int turns) {
        poisoned = true;
        poisonedTurnLeft = turns;
    }

    public int getCombo() {
        return combo;
    }

    void setTripleBonusToFalse() {
        this.tripleBonus = false;
    }

    void setTripleBonusTurnLeft() {
        this.tripleBonusTurnLeft = 0;
    }

    void removeNegativeSpells() {
        hpRegeneration = false;
        manaRegeneration = false;
        poisoned = false;
        poisonedTurnLeft = 0;
        hpRegenerationTurnLeft = 0;
        manaRegenerationTurnLeft = 0;
    }

    public String getPlayerState() {
        StringBuilder states = new StringBuilder("");
        if (hpRegeneration) {
            states.append("HP Regen;");
        }
        if (manaRegeneration) {
            states.append("Mana Regen;");
        }
        if (poisoned) {
            states.append("Poisoned;");
        }
        if (tripleBonus) {
            states.append("Triple Points");
        }
        return states.toString();
    }

    void activateTripleBonus(int turns) {
        tripleBonus = true;
        tripleBonusTurnLeft = turns;
    }


    public void initializeLessons(List<Lesson> lessons) {
        miniLessons = MiniLessons.getMiniLessons(lessons);
    }

    public String getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(String lessonTitle) {
        selectedLesson = lessonTitle;
    }

    public String getName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String name;
        try {
            name = preferences.getString("username", Config.DEFAULT_USER_NAME);
        } catch (ClassCastException cce) {
            name = context.getString(R.string.unknownPlayerName);
        }
        return name;
    }

    public boolean isLessonsInitialized() {
        return (miniLessons != null);
    }

    public void setSelectedPattern(Pattern selectedPattern) {
        this.currentPattern = selectedPattern;
    }

    public Pattern getCurrentPattern() {
        return currentPattern;
    }

    void addBonus() {
        switch (BonusType.RANDOM) {
            case RANDOM:
                int bonus = new Random().nextInt(player.game.getLevel());
                bonus += (player.getHealth() + player.getMana() + new Random().nextInt(4));
                player.addScore(bonus);
                Log.d(TAG, "Random bonus: " + bonus + "points.");
            default:
                Log.d(TAG, "No bonus");
        }
    }

    public void addBonus(int extra) {
        switch (BonusType.UNFREEZE) {
            case UNFREEZE:
                int bonus = extra * Config.BONUS_POINTS_UNFREEZE;
                player.addScore(bonus);
                Log.i(TAG, "Bonus for unfreeze: " + bonus + "points.");
            default:
                Log.i(TAG, "No bonus");
        }
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public String getDbErrorMessage() {
        String tmp = dbErrorMessage;
        dbErrorMessage = "";
        return tmp;
    }

    public void addHealthPerLevel() {
        if (player.getHealth() < 100) {
            int health = player.getHealth() + (Config.HEALTH_BONUS_PER_LEVEL);
            player.setHealth((health > 100 ? 100 : health));
        }
    }
}
