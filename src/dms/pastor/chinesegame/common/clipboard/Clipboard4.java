package dms.pastor.chinesegame.common.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 * Created 03/08/2012
 */
public final class Clipboard4 {

    public boolean saveText(final Context context, final String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("textClipboard", text);
            clipboard.setPrimaryClip(clip);
            return true;
        }
        return false;
    }

}
