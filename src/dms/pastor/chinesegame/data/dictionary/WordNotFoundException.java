package dms.pastor.chinesegame.data.dictionary;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Email: email can be found on my website
 */
class WordNotFoundException extends RuntimeException {

    public WordNotFoundException(int id) {
        super("Word not found for id:" + id);
    }
}
