package dms.pastor.chinesegame.data.dictionary;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * Email: email can be found on my website
 */
class WordNotFoundException extends RuntimeException {

    public WordNotFoundException(int id) {
        super("Word not found for id:" + id);
    }
}
