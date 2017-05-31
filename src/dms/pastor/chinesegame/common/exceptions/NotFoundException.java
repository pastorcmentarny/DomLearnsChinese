package dms.pastor.chinesegame.common.exceptions;

/**
 * Author Dominik Symonowicz
 * Created 01/10/2013 16:36
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p/>
 * Not Found Exception used for indicate that abnormal lack of items in collection of .. lessons and etc.
 */
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);

    }
}
