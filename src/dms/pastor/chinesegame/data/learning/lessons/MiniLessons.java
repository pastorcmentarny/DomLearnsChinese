package dms.pastor.chinesegame.data.learning.lessons;

import android.util.Log;

import java.util.List;

import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.utils.Result;

/**
 * User: Pastor
 * Date: 03.01.13
 * Time: 19:39
 */
public final class MiniLessons {

    private static final String TAG = "DOMS MiniLessons";
    private static MiniLessons miniLessons = null;
    private final List<Lesson> lessons;
    private Lesson currentLesson = null;

    private MiniLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public static synchronized MiniLessons getMiniLessons(List<Lesson> lessons) {
        if (lessons != null && miniLessons == null) {
            miniLessons = new MiniLessons(lessons);
        }
        return miniLessons;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Lesson getCurrentLesson() {
        return currentLesson;
    }

    public void setCurrentLesson(Lesson currentLesson) {
        this.currentLesson = currentLesson;
    }

    //TODO  player.miniLessons.getLessons() should be getLesson and
    //TODO  player.miniLessons.setCurrentLesson(currentLesson);
    public Result findLesson(String selectedLesson) {
        Player player = Player.getPlayer();
        if (player.isLessonsInitialized()) {
            for (Lesson l : player.miniLessons.getLessons()) {
                if (l.getTitle().equalsIgnoreCase(selectedLesson)) {
                    currentLesson = l;
                    player.miniLessons.setCurrentLesson(currentLesson);
                    break;
                }
            }
            if (miniLessons.getCurrentLesson() != null) {
                return new Result(true, "Lesson  initialized");
            } else {
                String msg = "Lesson NOT found";
                Log.d(TAG, msg);
                return new Result(false, msg);
            }
        } else {
            String msg = "Mini lesson are not initialized";
            Log.e(TAG, msg);
            return new Result(false, msg);
        }
    }
}