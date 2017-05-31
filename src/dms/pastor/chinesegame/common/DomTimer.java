package dms.pastor.chinesegame.common;

import java.util.Calendar;

/**
 * Author Dominik Symonowicz
 * Created 16/11/2012
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p/>
 * Not Found Exception used for indicate that abnormal lack of items in collection of .. lessons and etc.
 */
public final class DomTimer {
    private long start;
    private long finish;

    public void start() {
        finish = 0;
        start = Calendar.getInstance().getTimeInMillis();
    }

    public long calcTotalTime() {
        return finish - start;
    }

    public long calcCurrentTime() {
        return Calendar.getInstance().getTimeInMillis() - start;
    }

    public void stop() {
        finish = Calendar.getInstance().getTimeInMillis();
    }

    public void resetTimer() {
        start = 0;
        finish = 0;
    }

}
