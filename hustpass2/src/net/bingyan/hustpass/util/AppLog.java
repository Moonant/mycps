package net.bingyan.hustpass.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ant on 14-8-3.
 */
public class AppLog {
    String className = "";
    static String TAG = "true";

    static boolean DEBUG = false;

    public AppLog(String tag) {
        this(tag, null);
    }

    public AppLog(Class<?> clazz) {
        this(null, clazz);
    }

    public AppLog(String tag, Class<?> clazz) {
        if (!TextUtils.isEmpty(tag))
            TAG += tag;
        if (clazz != null)
            className = '[' + clazz.getSimpleName() + ']';
    }

    public void v(String msg) {
        if(!DEBUG){
            return;
        }

        if (msg != null) {
            Log.v(TAG, className + msg);
        } else {
            Log.v(TAG, msg);
        }
    }

    static public void appv(String msg){
        if(!DEBUG){
            return;
        }
        Log.v(TAG, msg);
    }
}


