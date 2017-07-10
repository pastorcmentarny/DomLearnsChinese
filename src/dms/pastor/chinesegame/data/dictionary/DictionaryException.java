package dms.pastor.chinesegame.data.dictionary;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 27/06/2017
 */
class DictionaryException extends IllegalStateException {
    DictionaryException() {
        super("Cannot search due lack of dictionary.Error?");
    }
}
