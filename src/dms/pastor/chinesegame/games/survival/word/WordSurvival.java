package dms.pastor.chinesegame.games.survival.word;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.common.DomTimer;
import dms.pastor.chinesegame.common.clipboard.Clipboard4;
import dms.pastor.chinesegame.data.Statistic;
import dms.pastor.chinesegame.data.dictionary.Dictionary;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.data.game.Level;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.game.Spells;
import dms.pastor.chinesegame.data.game.score.HighScore;
import dms.pastor.chinesegame.games.calculator.SurvivalScoreCalculator;
import dms.pastor.chinesegame.games.survival.SurvivalResult;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.Result;
import dms.pastor.chinesegame.utils.UIUtils;
import dms.pastor.chinesegame.utils.Utils;

import static android.widget.Toast.LENGTH_SHORT;
import static dms.pastor.chinesegame.Config.COMBO_MINIMUM;
import static dms.pastor.chinesegame.Config.RANDOM_EVENT_FREQ;
import static dms.pastor.chinesegame.Config.RANDOM_SIZE;
import static dms.pastor.chinesegame.data.game.Spells.castShowPinyinHideCharacter;
import static dms.pastor.chinesegame.data.game.Spells.castSpellShowPinyin;
import static dms.pastor.chinesegame.data.game.Spells.doubleHP;
import static dms.pastor.chinesegame.data.game.Spells.doubleMP;
import static dms.pastor.chinesegame.data.game.Spells.eventHappen;
import static dms.pastor.chinesegame.data.game.Spells.halfHP;
import static dms.pastor.chinesegame.data.game.Spells.halfMP;
import static dms.pastor.chinesegame.utils.UIUtils.displayHalfHalfToast;
import static dms.pastor.chinesegame.utils.UIUtils.setAllToDefault;
import static dms.pastor.chinesegame.utils.UIUtils.setBackgroundColor;
import static dms.pastor.chinesegame.utils.UIUtils.setFrozen;
import static dms.pastor.chinesegame.utils.UIUtils.setIncorrect;
import static dms.pastor.chinesegame.utils.UIUtils.setRemoved;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;
import static dms.pastor.chinesegame.utils.UIUtils.setToDefault;
import static dms.pastor.chinesegame.utils.UIUtils.setUnTapButton;
import static java.lang.String.format;

/**
 * Author: Pastor
 * Created at:
 * Date: 14.11.12
 * Time: 23:19
 */
public final class WordSurvival extends Level implements View.OnClickListener {
    private static final String TAG = Config.TAG_PREFIX + "WORD SURVIVAL LEVEL";
    private final Spells eventSpells = new Spells(this, this);
    private final SurvivalScoreCalculator calculator = new SurvivalScoreCalculator();
    private SharedPreferences preferences;
    private SharedPreferences settings;
    private Clipboard4 clipboard;
    private ArrayList<Word> words;
    private Player player;
    private int fail = Config.DEFAULT_FAIL_POINTS;
    private boolean woops = false;
    private int comboPoints = 0;
    private boolean frozen = false;
    private DomTimer timer;
    private Vibrator vibrator;
    private Word answerWord;
    private Word wrongWord1;
    private Word wrongWord2;
    private Word wrongWord3;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button showPinyinSpellButton;
    private Button healSpellButton;
    private Button removeBadAnswerButton;
    private Button cureSpellButton;
    private TextView currentPinyin;
    private Statistic statistic;
    private int wallHP = 0;
    private int wallHPTotal = 0;
    private boolean onlyPinyin = false;
    private LinearLayout levelMain;
    private TextView score;
    private TextView bonusScore;
    private TextView life;
    private TextView lifePenalty;
    private TextView lvl;
    private TextView mana;
    private TextView status;
    private TextView debugInfo;
    private TextView timeElapsed;
    private TextView mistakesValue;
    private TextView currentCharacter;
    private Button tap2unfreeze;
    private HighScore highScore;
    private boolean pinyinUsed = false;
    private TextView correctValue;
    private Result result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "creating..");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.level);

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        statistic = Statistic.getStatistic(this);

        levelMain = (LinearLayout) findViewById(R.id.level_main);

        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);

        bonusScore = (TextView) findViewById(R.id.bonus_score);
        correctValue = (TextView) findViewById(R.id.correct_value);
        cureSpellButton = (Button) findViewById(R.id.spell_cure_button);
        currentCharacter = (TextView) findViewById(R.id.currentCharacter);
        currentPinyin = (TextView) findViewById(R.id.currentPinyin);
        debugInfo = (TextView) findViewById(R.id.player_states);
        healSpellButton = (Button) findViewById(R.id.spell_heal_button);
        life = (TextView) findViewById(R.id.current_life);
        lifePenalty = (TextView) findViewById(R.id.bonus_life);
        lvl = (TextView) findViewById(R.id.current_lvl);
        mana = (TextView) findViewById(R.id.mana_value);
        mistakesValue = (TextView) findViewById(R.id.mistakes_value);
        removeBadAnswerButton = (Button) findViewById(R.id.spell_remove_wrong_button);
        score = (TextView) findViewById(R.id.current_score);
        showPinyinSpellButton = (Button) findViewById(R.id.spell_show_pinyin_button);
        status = (TextView) findViewById(R.id.levelStatus);
        timeElapsed = (TextView) findViewById(R.id.time_elasped_value);
        tap2unfreeze = (Button) findViewById(R.id.tap2unfreeze);
        highScoreTextView = (TextView) findViewById(R.id.high_score);
        highScoreTextView.setVisibility(View.VISIBLE);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        showPinyinSpellButton.setOnClickListener(this);
        healSpellButton.setOnClickListener(this);
        removeBadAnswerButton.setOnClickListener(this);
        cureSpellButton.setOnClickListener(this);
        tap2unfreeze.setOnClickListener(this);
        currentCharacter.setOnClickListener(this);

        AdView adView = (AdView) this.findViewById(R.id.adView);
        try {
            //set ads

            com.google.android.gms.ads.AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(getString(R.string.am_e), getString(R.string.am_e_init) + e.getMessage());
        }

        setup();
        highScore = HighScore.getHighScore();
        clipboard = new Clipboard4();

        setupUI();
        setupTurn();
        updatePlayer();

        super.runTimer();

    }

    @Override
    public void onPause() {
        super.onPause();
        turnOffVibrator();
        turnOfTimer();
        statistic.save();
        turnOffMediaPlayer();
    }

    private void turnOffMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    private void turnOfTimer() {
        if (myTimer != null) {
            myTimer.cancel();
        }
    }

    private void turnOffVibrator() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        final Animation castSpell = AnimationUtils.loadAnimation(this, R.anim.cast_spell);
        statistic.save();
        switch (view.getId()) {
            case R.id.answer1:
                checkAnswer(answer1Button);
                break;
            case R.id.answer2:
                checkAnswer(answer2Button);
                break;
            case R.id.answer3:
                checkAnswer(answer3Button);
                break;
            case R.id.answer4:
                checkAnswer(answer4Button);
                break;
            case R.id.spell_show_pinyin_button:
                castShowPinyinSpell(view, castSpell);
                break;
            case R.id.spell_heal_button:
                castHealSpell(view, castSpell);
                break;
            case R.id.spell_remove_wrong_button:
                castRemoveWrongAnswerSpell(view, castSpell);
                break;
            case R.id.spell_cure_button:
                castCureSpell(view, castSpell);
                break;
            case R.id.tap2unfreeze:
                unfreezeAction();
                break;
            case R.id.currentCharacter:
                saveCurrentCharacterToClipboard();
                break;
            default:
                Toast.makeText(this, getString(R.string.woops), LENGTH_SHORT).show();
        }
    }

    private void saveCurrentCharacterToClipboard() {
        if (preferences.getBoolean("copy2clipboard", false)) {
            final boolean saved = clipboard.saveText(this, currentCharacter.getText().toString());
            String message;
            if (saved) {
                message = getString(R.string.copied2clipboard);
            } else {
                message = getString(R.string.error_unable_to_copy_to_clipboard);
            }
            Toast.makeText(this, message, LENGTH_SHORT).show();
        }
    }

    private void unfreezeAction() {
        wallHP--;
        player.addScore(5);
        if (wallHP <= 0) {
            unFreeze();
        } else {
            status.setText(format(Locale.ENGLISH, "%s  %d  %s", getString(R.string.msg_un_tap_1), wallHP, getString(R.string.msg_un_tap_2)));
            setTextColor(status, R.color.tap_button, this);
        }
        updatePlayer();
    }

    private void castCureSpell(View view, Animation castSpell) {
        view.startAnimation(castSpell);
        statistic.addCureUsed();
        Spells.castCure(status);
        setupUI();
        updateUI();
    }

    private void castRemoveWrongAnswerSpell(View view, Animation castSpell) {
        view.startAnimation(castSpell);
        statistic.addRemoveWrongAnswerUsed();
        castSpellHalf();
    }

    private void castHealSpell(View view, Animation castSpell) {
        view.startAnimation(castSpell);
        statistic.addHealUsed();
        eventSpells.castHeal(status, this);
        updatePlayer();
    }

    private void castShowPinyinSpell(View view, Animation castSpell) {
        view.startAnimation(castSpell);
        statistic.addPinyinUsed();
        result = castSpellShowPinyin(currentPinyin, showPinyinSpellButton);
        if (result.isSuccess()) {
            status.setText(result.getMessage());
            setTextColor(status, R.color.good_news, this);
            pinyinUsed = true;
        } else {
            status.setText(result.getMessage());
            setTextColor(status, R.color.bad_news, this);
        }
        updatePlayer();
    }

    @Override
    public void setupTurn() {
        Log.d(TAG, "Set stuff for level " + player.game.getLevel());
        fail = Config.reduceFail(fail);
        woops = false;
        comboPoints = 0;
        pinyinUsed = false;
        onlyPinyin = false;
        frozen = false;
        DomUtils.shuffle(words);
        setTexts();
        restartTimer();

        setComboPoints();
        checkForBonusPointsFor50Levels();
        generateEvent();

        String status = player.nextTurn();
        Log.i(TAG, status);
    }

    private void restartTimer() {
        timer = new DomTimer();
        timer.resetTimer();
        timer.start();
    }

    private void checkForBonusPointsFor50Levels() {
        if (player.game.getLevel() % 50 == 0) {
            player.addScore((int) (player.game.getStage().getScoreBonusMultiply() * 25));
            Toast.makeText(this, "Extra bonus every 50 levels :" + (player.game.getStage().getScoreBonusMultiply() * 25), LENGTH_SHORT).show();
        }
    }

    private void generateEvent() {
        if (player.game.getLevel() % RANDOM_EVENT_FREQ == 0) {
            event(new Random().nextInt(RANDOM_SIZE));
        }
    }

    private void setComboPoints() {
        if (player.getCombo() > COMBO_MINIMUM) {
            status.setText(format(Locale.ENGLISH, "%s %d %s %d", getString(R.string.combo), player.getCombo(), getString(R.string.combo_points), player.getCurrentComboBonus(player.game.getLevel())));
            setTextColor(status, R.color.combo, this);
        } else {
            status.setText("");
        }
    }

    private void setTexts() {
        answer1Button.setText(words.get(0).getWordInEnglish());
        answer2Button.setText(words.get(1).getWordInEnglish());
        answer3Button.setText(words.get(2).getWordInEnglish());
        answer4Button.setText(words.get(3).getWordInEnglish());
    }


    @SuppressWarnings("deprecation")
    @Override
    public void setupUI() {
        setToDefault(this, answer1Button);
        setToDefault(this, answer2Button);
        setToDefault(this, answer3Button);
        setToDefault(this, answer4Button);
        if (Build.VERSION.SDK_INT < 23) {
            showPinyinSpellButton.setTextAppearance(this, R.style.spell_button_style);
        } else {
            showPinyinSpellButton.setTextAppearance(R.style.spell_button_style);
        }
        currentPinyin.setVisibility(TextView.INVISIBLE);
        currentCharacter.setText(answerWord.getChineseCharacter());
        currentCharacter.setVisibility(TextView.VISIBLE);
        currentPinyin.setText(answerWord.getPinyin());
        correctValue.setText(String.valueOf(player.game.getCorrect()));
        mistakesValue.setText(String.valueOf(player.game.getMistake()));
        score.setText(String.valueOf(player.getScore()));
        bonusScore.setText("");
        life.setText(String.valueOf(player.getHealth()));
        lifePenalty.setText(String.valueOf(player.getHealth()));
        mana.setText(String.valueOf(player.getMana()));
        lvl.setText(String.valueOf(player.game.getLevel()));
        setLifeColor();
        setManaColor();
    }

    @Override
    public void updatePlayer() {
        if (player.isAlive()) {
            updateUI();
        } else {
            dead();
        }
    }

    @Override
    protected void updateUI() {

        score.setText(String.valueOf(player.getScore()));

        int hsPlace = highScore.getCurrentPlaceFor(player.getScore(), player.game.getGameType());
        if (hsPlace > 0) {
            this.highScoreTextView.setText(format(Locale.ENGLISH, " HS:[ %d ] ", hsPlace));
            setHSPlaceColor(hsPlace);
        }

        int bonusNow = calculator.calculate(player.game, timer.calcCurrentTime());

        if (onlyPinyin) {
            bonusNow += Config.BONUS_ONLY_PINYIN;
        }

        if (bonusNow >= 0) {
            setTextColor(status, R.color.good_news, this);
        } else {
            setTextColor(status, R.color.bad_news, this);
        }

        bonusScore.setText(format("( %s )", (bonusNow >= 0 ? "+" + bonusNow : bonusNow)));

        life.setText(String.valueOf(player.getHealth()));
        checkHealthPenalty();
        mana.setText(String.valueOf(player.getMana()));

        setFrozenButtons();


        debugInfo.setText(player.getPlayerState());
        setTextColor(debugInfo, R.color.status, this);

        timeElapsed.setText(String.valueOf(timer.calcCurrentTime() / 1000));
        correctValue.setText(String.valueOf(player.game.getCorrect()));
        mistakesValue.setText(String.valueOf(player.game.getMistake()));
        answer1Button = checkIsButtonUsed(answer1Button);
        answer2Button = checkIsButtonUsed(answer2Button);
        answer3Button = checkIsButtonUsed(answer3Button);
        answer4Button = checkIsButtonUsed(answer4Button);

        setLifeColor();
        setManaColor();
    }

    private void setFrozenButtons() {
        if (frozen) {
            answer1Button.setEnabled(false);
            answer2Button.setEnabled(false);
            answer3Button.setEnabled(false);
            answer4Button.setEnabled(false);
            showPinyinSpellButton.setEnabled(false);
            healSpellButton.setEnabled(false);
            removeBadAnswerButton.setEnabled(false);
            cureSpellButton.setEnabled(false);
        } else {
            if (pinyinUsed) {
                UIUtils.setUsed(this, showPinyinSpellButton);
            } else {
                UIUtils.setButtonEnabled(this, showPinyinSpellButton, player.getMana() >= Config.PINYIN_SPELL_COST);
            }

            UIUtils.setButtonEnabled(this, healSpellButton, player.getMana() >= Config.HEAL_SPELL_COST && player.getHealth() <= 100);
            UIUtils.setButtonEnabled(this, removeBadAnswerButton, player.getMana() >= Config.REMOVE_BAD_ANSWER_SPELL_COST);
            UIUtils.setButtonEnabled(this, cureSpellButton, player.getMana() >= Config.CURE_SPELL_COST);
        }
    }

    @Override
    public void setup() {
        player = Player.getPlayer();
        setNewQuestion();
        if (isCheeseCakeLevel()) {
            cheesecakeLevel();
        }

        Log.d("DIFF LEVEL CHECK:", "" + answerWord.getDifficulty() + wrongWord1.getDifficulty() + wrongWord2.getDifficulty() + wrongWord3.getDifficulty());
    }

    private void setNewQuestion() {
        words = new ArrayList<>();
        do {
            answerWord = player.game.getRandomWordForLevel();
        } while (answerWord == null);
        wrongWord1 = selectAWord(wrongWord1, new Word[]{answerWord});
        wrongWord2 = selectAWord(wrongWord2, new Word[]{answerWord, wrongWord1});
        wrongWord3 = selectAWord(wrongWord3, new Word[]{answerWord, wrongWord1, wrongWord2});
        words.add(answerWord);
        words.add(wrongWord1);
        words.add(wrongWord2);
        words.add(wrongWord3);
        freezeIfWordsContainsWordCold(words);
    }

    private void freezeIfWordsContainsWordCold(ArrayList<Word> words) {
        for (Word word : words) {
            if (word.getWordInEnglish().equalsIgnoreCase("cold")) {
                player.setCold(true);
                Toast.makeText(this, "There is a word so cold that it frozen your button", LENGTH_SHORT).show();
            }
        }
    }


    private void event(int random) {

        switch (random) {
            case 3:
            case 33:
            case 66:
            case 99:
                tripleEvent();
                break;
            case 13:
                jackpotEvent();
                break;
            case 47:
            case 87:
                drainManaEvent();
                break;
            case 14:
            case 21:
            case 28:
            case 35:
            case 56:
            case 63:
                addMinorBonusEvent();
                break;
            case 4:
            case 22:
            case 44:
                poisonEvent();
                break;
            case 88:
                doubleHappinessEvent();
                break;
            case 9:
            case 36:
            case 54:
            case 72:
                regenerationEvent();
                break;
            case 7:
            case 77:
                shitHappenEvent();
                break;
            case 11:
                swapEvent();
                break;
            case 17:
            case 19:
            case 23:
            case 29:
                frozenEvent();
                break;
            case 81:
            case 82:
            case 84:
                painKillerEvent();
                break;
            case 71:
            case 79:
            case 83:
                blindEvent();
                break;
            case 101:
            case 102:
            case 103:
            case 104:
                showPinyinEvent();
                break;
            case 91:
            case 92:
            case 93:
            case 94:
                halfHalfEvent();
                break;
            case 121:
            case 122:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
                showPinyinHideCharacterEvent();
                break;
            case 151:
            case 152:
            case 153:
                doubleHpEvent();
                break;
            case 144:
                halfHpEvent();
                break;
            case 154:
            case 155:
                doubleMpEvent();
                break;
            case 157:
            case 158:
                halfMpEvent();
                break;
            default:
                Log.i(TAG, "nothing happen");
        }
    }

    private void halfMpEvent() {
        Log.i(TAG, "generating event ..half MP");
        statistic.addHalfMP();
        halfMP();
        eventHappen(getString(R.string.cast_half_mp), false);
        updateUI();
    }

    private void doubleMpEvent() {
        Log.i(TAG, "generating event ..double MP");
        statistic.addDoubleMP();
        doubleMP();
        eventHappen(getString(R.string.cast_double_mp), true);
        updateUI();
    }

    private void halfHpEvent() {
        Log.i(TAG, "generating event ..half HP");
        statistic.addHalfHP();
        halfHP();
        eventHappen(getString(R.string.cast_half_hp), false);
        updateUI();
    }

    private void doubleHpEvent() {
        Log.i(TAG, "generating event ..double HP");
        statistic.addDoubleHP();
        doubleHP();
        eventHappen(getString(R.string.cast_double_hp), true);
        updateUI();
    }

    private void showPinyinHideCharacterEvent() {
        Log.i(TAG, "generating event ..show pinyin hide character");
        statistic.addShowPinyinHideCharacter();
        result = castShowPinyinHideCharacter(currentCharacter, currentPinyin, showPinyinSpellButton);
        eventHappen(getString(R.string.cast_show_pinyin_hide_character), false);
        pinyinUsed = true;
        onlyPinyin = true;
        updateUI();
    }

    private void halfHalfEvent() {
        Log.i(TAG, "generating event ..show half half");
        statistic.addHalfHalfHappen();
        displayHalfHalfToast(this, this);
        castHalf();
    }

    private void showPinyinEvent() {
        Log.i(TAG, "generating event ..show pinyin");
        statistic.addShowPinyin();
        result = castSpellShowPinyin(currentPinyin, showPinyinSpellButton);
        eventHappen(getString(R.string.cast_show_pinyin), true);
        pinyinUsed = true;
        updateUI();
    }

    private void blindEvent() {
        Log.i(TAG, "generating event ..blind");
        statistic.addBlind();
        Spells.blind(answer1Button, answer2Button, answer3Button, answer4Button);        //TEST IT
        eventHappen(getString(R.string.cast_blind), false);
        updatePlayer();
    }

    private void painKillerEvent() {
        Log.i(TAG, "generating event ..pain killer");
        statistic.addPainKiller();
        fail = Config.DEFAULT_FAIL_POINTS;
        eventHappen(getString(R.string.cast_painKiller), true);
    }

    private void frozenEvent() {
        Log.i(TAG, "generating event ..frozen");
        statistic.addFrozen();
        freeze();
        eventHappen(getString(R.string.cast_frozen), false);
        updatePlayer();
    }

    private void swapEvent() {
        Log.i(TAG, "generating event ..swap hp with mana");
        statistic.addSwapHpMp();
        eventSpells.castSwap();
        eventHappen(getString(R.string.cast_swap), false);
        updatePlayer();
    }

    private void shitHappenEvent() {
        Log.i(TAG, "generating event .. shit happens");
        statistic.addShitHappens();
        eventSpells.castShitHappens();
        eventHappen(getString(R.string.cast_shit_happens), false);
        updatePlayer();
    }

    private void regenerationEvent() {
        Log.i(TAG, "generating event ..get regeneration");
        statistic.addRegeneration();
        eventSpells.castRegeneration();
        eventHappen(getString(R.string.cast_regeneration), true);
        updatePlayer();
    }

    private void doubleHappinessEvent() {
        Log.i(TAG, "generating event .. get poisoned");
        statistic.addPainKiller();
        statistic.addMinorPoints();
        player.setHealth(100);
        eventHappen("Double 88,Double happiness", true); //TODO document this
    }

    private void poisonEvent() {
        Log.i(TAG, "generating event .. get poisoned");
        statistic.addPoisonHappen();
        eventSpells.castPoison();
        eventHappen(getString(R.string.cast_poison), false);
        updatePlayer();
    }

    private void addMinorBonusEvent() {
        Log.i(TAG, "generating event ..add minor points " + Config.DEFAULT_BONUS_POINTS);
        statistic.addMinorPoints();
        int minorBonus = eventSpells.castMinorPointsBonus();
        eventHappen(getString(R.string.cast_minor_bonus) + minorBonus + ")!", true);
        updatePlayer();
    }

    private void drainManaEvent() {
        Log.i(TAG, "generating event ..drain mana");
        statistic.addDrainMana();
        eventSpells.castManaDrain();
        eventHappen(getString(R.string.cast_mana_drain), false);
        updatePlayer();
    }

    private void jackpotEvent() {
        Log.i(TAG, "generating event ..jackpot");
        statistic.addJackpot();
        int jackpot = Config.calcJackPot(player.game.getLevel());
        player.setScore(player.getScore() + jackpot);
        Toast.makeText(this, getString(R.string.cast_teleport) + jackpot + ")", LENGTH_SHORT).show();
        endOfLevel();
    }

    private void tripleEvent() {
        Log.i(TAG, "generating event ..tripe");
        statistic.addTripe();
        eventSpells.castTriple();
        eventHappen(getString(R.string.cast_triple), true);
        updatePlayer();
    }


    private void castHalf() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(answer1Button);
        buttons.add(answer2Button);
        buttons.add(answer3Button);
        buttons.add(answer4Button);
        ArrayList<Button> temp = new ArrayList<>();
        for (Button button : buttons) {
            if (isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {
                temp.add(button);
            }
        }
        buttons.removeAll(temp);
        buttons = UIUtils.shuffleButtons(buttons);
        try {
            buttons.remove(0);
            for (Button button : buttons) {
                setRemoved(this, this, button);
            }
        } catch (IndexOutOfBoundsException ioobe) {
            Log.w(TAG, getString(R.string.e_ioobe) + ioobe.getMessage());
        }
        updatePlayer();


    }

    private void setLifeColor() {
        if (player.getHealth() > 100) {
            setTextColor(life, R.color.very_high_hp, this);
        } else if (player.getHealth() > 60) {
            setTextColor(life, R.color.high_hp, this);
        } else if (player.getHealth() > 40) {
            setTextColor(life, R.color.medium_hp, this);
        } else if (player.getHealth() > 20) {
            setTextColor(life, R.color.low_hp, this);
        } else {
            setTextColor(life, R.color.very_low_hp, this);
        }
    }

    private void setManaColor() {
        if (player.getMana() > 75) {
            setTextColor(mana, R.color.high_mp, this);
        } else if (player.getMana() > 50) {
            setTextColor(mana, R.color.medium_mp, this);
        } else if (player.getMana() > 25) {
            setTextColor(mana, R.color.low_mp, this);
        } else {
            setTextColor(mana, R.color.very_low_mp, this);
        }
    }

    private void castSpellHalf() {
        if (player.getMana() >= Config.REMOVE_BAD_ANSWER_SPELL_COST) {
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(answer1Button);
            buttons.add(answer2Button);
            buttons.add(answer3Button);
            buttons.add(answer4Button);
            buttons = DomUtils.shuffleButtons(buttons);
            for (int i = 0; i < buttons.size(); i++) {
                if (disableWrongButton(buttons.get(i))) {
                    i = buttons.size();
                }
            }
        }
        updatePlayer();
    }


    private boolean disableWrongButton(Button button) {
        if (!button.getText().toString().equals(answerWord.getWordInEnglish())) {
            if (button.isEnabled()) {
                player.setMana(player.getMana() - Config.REMOVE_BAD_ANSWER_SPELL_COST);
                setRemoved(this, this, button);
                return true;
            }
        }
        return false;
    }

    private void freeze() {
        player.setCold(true);
        frozen = true;
        wallHP = new Random().nextInt(Config.FREEZE_RANGE) + 1;
        wallHPTotal = wallHP;
        setFrozenButton(answer1Button);
        setFrozenButton(answer2Button);
        setFrozenButton(answer3Button);
        setFrozenButton(answer4Button);
        setFrozenButton(removeBadAnswerButton);
        setFrozenButton(cureSpellButton);
        setFrozenButton(healSpellButton);
        setFrozenButton(showPinyinSpellButton);
        setBackgroundColor(levelMain, R.color.freeze_background, this);
        tap2unfreeze.setVisibility(View.VISIBLE);
        setTextColor(status, R.color.bad_news, this);
        setEnabled(false);
    }

    private void setFrozenButton(Button button) {
        setFrozen(this, button, this);
        setUnTapButton(this, tap2unfreeze);
    }

    private void unFreeze() {
        frozen = false;
        player.setCold(false);
        setEnabled(true);
        levelMain.setBackgroundColor(Color.BLACK);
        tap2unfreeze.setVisibility(View.GONE);
        UIUtils.setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_button));
        status.setText(getString(R.string.msg_defrost));
        setTextColor(status, R.color.status, this);
        player.addBonus(wallHPTotal);
        setTexts();
        setAllToDefault(this, answer1Button, answer2Button, answer3Button, answer4Button);
        removeBadAnswerButton.setText(getResources().getString(R.string.remove_bad_answer));
        cureSpellButton.setText(getResources().getString(R.string.spell_cure));
        healSpellButton.setText(getResources().getString(R.string.spell_heal));
        showPinyinSpellButton.setText(getResources().getString(R.string.spell_show_pinyin));
        removeBadAnswerButton.setTextColor(Color.WHITE);
        cureSpellButton.setTextColor(Color.WHITE);
        healSpellButton.setTextColor(Color.WHITE);
        showPinyinSpellButton.setTextColor(Color.WHITE);
        updateUI();
    }

    private void checkAnswer(Button button) {
        Log.d(TAG, "check answer ..");
        if (super.isCorrectAnswer(button.getText().toString(), answerWord.getWordInEnglish())) {
            if (isCheeseCakeLevel()) {
                Toast.makeText(this, "Yummy Yummy", Toast.LENGTH_SHORT).show();
                player.addScore(88);
            }
            timer.stop();
            setEnabled(false);
            long time = timer.calcTotalTime();
            player.game.addToTotalTime(time);

            int healthPenalty = Config.calcPenaltyHealthForTime(time);


            if (healthPenalty > Config.NO_PENALTY_TIME) {
                player.setHealth(player.getHealth() - (healthPenalty - Config.NO_PENALTY_TIME));
                woops = true;
            } else {
                player.setMana(player.getMana() + 1);
            }

            if (player.isDead()) {
                dead();
            } else {
                endOfLevel();
            }

        } else {
            if (vibrator != null && settings.getBoolean("vibrate", Config.DEFAULT_VIBRATE)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (settings.getBoolean("playSound", Config.DEFAULT_PLAY_SOUND)) {
                playTestTune(this);
            }
            player.game.addMistake();

            setIncorrect(this, this, button);

            woops = true;
            player.setHealth(player.getHealth() - fail);

            if (player.isAlive()) {
                fail = Config.increaseFail(fail);
                player.addScore(-5);
            } else {
                dead();
            }
            updatePlayer();
        }
    }


    public void endOfLevel() {
        StringBuilder sb = new StringBuilder("");
        int totalBonus = calculator.calculate(player.game, timer.calcTotalTime());
        if (woops) {
            if (player.getCombo() > COMBO_MINIMUM) {
                totalBonus = addComboPoints(totalBonus);
                sb.append(getString(R.string.combo_bonus)).append(comboPoints).append("!");
                statistic.addWordToWordMistake(getWordIdFromWrong(words, answerWord.getWordInEnglish()));
                if (onlyPinyin) {
                    player.addScore(Config.BONUS_ONLY_PINYIN);
                    sb.append(" Extra bonus for pinyin: ").append(Config.BONUS_ONLY_PINYIN);
                }
            }
        } else {
            player.addCombo();
            player.game.addCorrect();
        }

        player.setScore((player.getScore() + totalBonus));

        if (sb.length() > 5) {
            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        }
        player.game.addLevel();

        player.addHealthPerLevel();
        player.setMana(player.getMana() + Config.MANA_BONUS_PER_LEVEL);
        //max health is 100,if current health is higher it will be reduce by 1 per turn
        if (player.getHealth() > 100) {
            player.setHealth(player.getHealth() - 1);
        }
        setupNewLevel();
    }

    private int addComboPoints(int totalBonus) {
        statistic.addToMaxCombo(player.getCombo());
        comboPoints = player.claimComboBonus(player.game.getLevel());
        statistic.addToMaxComboPoints(comboPoints);
        totalBonus = totalBonus + comboPoints;
        return totalBonus;
    }

    private void setupNewLevel() {
        Log.d(TAG, "setup a New Level ..");
        setEnabled(false);
        setup();
        setupUI();
        setupTurn();
        setEnabled(true);
        if (player.isCold()) {
            freeze();
        }
        updatePlayer();
    }

    protected void setEnabled(boolean enabled) {
        answer1Button.setEnabled(enabled);
        answer2Button.setEnabled(enabled);
        answer3Button.setEnabled(enabled);
        answer4Button.setEnabled(enabled);
        showPinyinSpellButton.setEnabled(enabled);
        healSpellButton.setEnabled(enabled);
        removeBadAnswerButton.setEnabled(enabled);
        cureSpellButton.setEnabled(enabled);
    }


    private void dead() {
        timer.stop();


        long time = timer.calcTotalTime();
        player.game.addToTotalTime(time);

        Intent ii;
        ii = new Intent(this, SurvivalResult.class);
        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
        finish();

    }

    private void checkHealthPenalty() {
        int healthPenalty = Config.calcPenaltyHealthForTime(timer.calcCurrentTime());
        if (healthPenalty > Config.NO_PENALTY_TIME) {
            lifePenalty.setText(format(Locale.ENGLISH, "(-%d)", (healthPenalty - Config.NO_PENALTY_TIME)));
        } else {
            lifePenalty.setText("");
        }
    }

    private Button checkIsButtonUsed(Button button) {
        if (button.getText().toString().equalsIgnoreCase(getResources().getString(R.string.removed))) {
            button.setText(getResources().getString(R.string.removed));
            setTextColor(button, R.color.removed_button, this);
            setBackgroundColor(button, R.color.transparent, this);
            button.setEnabled(false);
        } else if (button.getText().toString().equalsIgnoreCase(getResources().getString(R.string.incorrect))) {
            button.setText(getResources().getString(R.string.incorrect));
            setTextColor(button, R.color.error, this);
            setBackgroundColor(button, R.color.transparent, this);
            button.setEnabled(false);
        }
        return button;
    }

    private void cheesecakeLevel() {
        final String msg = "Lucky level means ... there is time for cheesecake";
        Log.i(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        answerWord = Dictionary.getDictionary().findWordById(743);
        wrongWord1 = Dictionary.getDictionary().findWordById(744);
        wrongWord2 = Dictionary.getDictionary().findWordById(745);
        wrongWord3 = Dictionary.getDictionary().findWordById(746);
        words.clear();
        words.add(answerWord);
        words.add(wrongWord1);
        words.add(wrongWord2);
        words.add(wrongWord3);

    }

    private boolean isCheeseCakeLevel() {
        return player.game.getLevel() == 88;
    }

}