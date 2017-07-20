package com.kk.utils;


import android.os.Handler;
import android.os.Looper;

/**
 * @author Lody
 *         <p>
 *         A set of tools for UI.
 */
public class UIUitls {
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());

    public static void post(Runnable r) {
        gUiHandler.post(r);
    }

    public static void postDelayed(long delay, Runnable r) {
        gUiHandler.postDelayed(r, delay);
    }
}
