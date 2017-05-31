package dms.pastor.chinesegame.data.game;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dms.pastor.chinesegame.Config;
import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.dictionary.Word;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

import static dms.pastor.chinesegame.data.game.Player.getPlayer;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * Email: email can be found on my website
 * <p>
 * Created: 28.11'2012
 */
public abstract class Level extends Activity {
    /*
     * performs specific task on each interval of time
     */
    private final Runnable timerTicker = new Runnable() {
        public void run() {
            updateUI();
        }
    };
    private final Player player = getPlayer();
    protected Timer myTimer;
    protected TextView highScoreTextView;
    protected MediaPlayer mediaPlayer = null;
    private boolean wasAlreadyPressed;

    public abstract void endOfLevel();

    /*
     run timer with task to do after time out
     */
    protected void runTimer() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }

        }, 0, Config.REFRESH);
    }

    /*
     *  I need to read why is done this way,but i
     */
    protected void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

    /**
     * setup turn elements (question,answer) for  level layout as by default,it has everything on.
     * This method must be always overwritten!
     */
    public abstract void setupTurn();

    /**
     * setup.
     */
    public abstract void setup();

    protected abstract void setEnabled(boolean enabled);


    /**
     * setup UI for basic level layout as by default,it has everything on.
     */
    public abstract void setupUI();

    /**
     * Updates player data on request
     * This method must be always overwritten!
     */
    public abstract void updatePlayer();

    /**
     * Updates data on UI on request
     * This method must be always overwritten!
     */
    protected abstract void updateUI();

    /*
     * Adding unique word to display on screen as possible answer
     */
    protected Word selectAWord(Word newWord, Word[] selectedAlreadyWords) {
        boolean stepA = true;
        while (stepA) {
            stepA = false;
            newWord = player.game.getRandomWordForLevel();
            for (Word alreadyUsedWord : selectedAlreadyWords) {
                if (newWord.equals(alreadyUsedWord)) {
                    stepA = true;
                }
            }
        }

        return newWord;
    }

    @Override
    public void onBackPressed() {
        if (wasAlreadyPressed) {
            DomUtils.goToHome(this, Level.this);
            super.onBackPressed();
        } else {
            String text = getResources().getString(R.string.back_to_quit);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            wasAlreadyPressed = true;
        }
    }

    protected boolean isCorrectAnswer(String answer, String correctAnswer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }

    protected void setHSPlaceColor(int place) {
        if (place > 75) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs75, this);
        } else if (place >= 50) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs50, this);
        } else if (place >= 40) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs40, this);
        } else if (place >= 30) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs30, this);
        } else if (place >= 20) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs20, this);
        } else if (place >= 10) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs10, this);
        } else if (place > 5) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs5, this);
        } else if (place == 3) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs3, this);
        } else if (place == 2) {
            UIUtils.setTextColor(highScoreTextView, R.color.hs2, this);
        } else {
            UIUtils.setTextColor(highScoreTextView, R.color.hs1, this);
        }
    }

    protected void playTestTune(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(volume, volume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        mediaPlayer.setLooping(false);

        try {
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://dms.pastor.chinesegame/" + R.raw.woops));
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalStateException ise) {
            mediaPlayer.stop();
            mediaPlayer.release();
            Log.e("Play Sound", "IllegalStateException occurred. " + ise.getMessage());
        } catch (IllegalArgumentException iae) {
            mediaPlayer.stop();
            mediaPlayer.release();
            Log.e("Play Sound", "IllegalArgumentException occurred. " + iae.getMessage());
        } catch (IOException ioe) {
            mediaPlayer.stop();
            mediaPlayer.release();
            Log.e("Play Sound", "IOException occurred. " + ioe.getMessage());
        }
    }

    protected int getWordIdFromWrong(ArrayList<Word> words, String wordAsString) {
        for (Word word : words) {
            if (word.getWordInEnglish().equalsIgnoreCase(wordAsString)) {
                return word.getId();
            }
        }
        Log.w("getWordIdFromWrong", "SOMETHING WENT VERY WRONG! Unable to find word from list of words.");
        return 0;
    }

}
