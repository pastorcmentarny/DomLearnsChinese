package dms.pastor.chinesegame.utils;

import android.util.Log;

/**
 * User: Pastor Cmentarny
 * Date: 02.03.13
 * Time: 18:47
 */
public final class Result {
    private boolean success;
    private String message;
    @SuppressWarnings("unused")
    private Object item;

    public Result(boolean success) {
        this.success = success;
        if (success) {
            message = "Success";
        } else {
            message = "Fail";
        }
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static Result failed() {
        return new Result(false);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFail() {
        return !success;
    }

    public String getMessage() {
        if (message != null) {
            return message;
        } else {
            return "Unknown";
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void update(Result r) {
        if (r.isFail()) {
            setSuccess(false);
            addMessage(r.getMessage());
        } else {
            addMessage(r.getMessage());
        }
    }

    public void addMessage(String message) {
        Log.i("RESULT/ addMessage", message);
        this.message += message + "\n";
    }

    public void updateResultForFalse(String message) {
        Log.i("updateResultForFalse", message);
        this.success = false;
        this.message += message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "\n\tsuccess: " + success +
                "\n\tmessage:" + message + '\'' +
                "\n\thasItem:" + (item != null) +
                "\n}";
    }

    @SuppressWarnings("SameParameterValue")
    public void set(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
