package com.anythingintellect.networklib.util;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class Log {
    private static final String TAG = "networkLib";

    public static void d(String msg) {
        android.util.Log.d(TAG, msg);
    }
}
