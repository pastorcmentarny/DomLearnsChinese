package dms.pastor.chinesegame.db;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import dms.pastor.chinesegame.AppLauncher;
import dms.pastor.chinesegame.R;

import static dms.pastor.chinesegame.utils.DomUtils.displayError;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Email: email can be found on my website
 * <p/>
 * Created 09.07.2016
 */
public class DictionaryHandler extends Handler {
    private static final String TAG = "DICTIONARY HANDLER";

    private final AppLauncher appLauncher;

    public DictionaryHandler(AppLauncher appLauncher) {
        this.appLauncher = appLauncher;
    }

    @SuppressWarnings("VariableNotUsedInsideIf")
    @Override
    public void handleMessage(Message msg) {
        try {
            dismissDialogIfCurrentlyShowing();
            logDBLoadStatus();
        } catch (IllegalArgumentException e) {
            displayError(appLauncher.getApplicationContext(), "Woops. Something went wrong.Try again.\nError message:" + e.getMessage());
        } catch (Exception e) {
            displayError(appLauncher.getApplicationContext(), "Woops. Something went terrible wrong.Try again.\nError message:" + e.getMessage());
        }

        Thread dictionaryLoaderThread = appLauncher.getDictionaryLoaderThread();
        interruptThreadIfDictionaryLoaderIsNotNull(dictionaryLoaderThread);
    }

    private void dismissDialogIfCurrentlyShowing() {
        ProgressDialog progressDialog = appLauncher.getProgressDialog();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void interruptThreadIfDictionaryLoaderIsNotNull(Thread dictionaryLoaderThread) {
        if (dictionaryLoaderThread != null) {
            dictionaryLoaderThread.interrupt();
        }
    }

    private void logDBLoadStatus() {
        if (appLauncher.getDbManager() != null) {
            Log.i(TAG, appLauncher.getString(R.string.msg_db_loaded));
        } else {
            Log.w(TAG, appLauncher.getString(R.string.e_failed_to_load_db));
        }
    }
}
