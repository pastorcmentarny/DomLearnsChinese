package dms.pastor.chinesegame.common.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Author Dominik Symonowicz
 * Created 03/08/2012
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
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
