package dms.pastor.chinesegame.common.exceptions;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 01/10/2013
 */
public class NullListException extends Exception {
    private static final String ERROR_MESSAGE = " is null.Please go to settings and press fix problems.If doesn't help,contact with me.Sorry for troubles :(";

    public NullListException() {
        super("Chat List" + ERROR_MESSAGE);

    }
}
