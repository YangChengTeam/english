package com.jarvanmo.exoplayerview.util;

import android.content.Context;

/**
 * Created by wanglin  on 2019/5/31 17:06.
 */
public class ScreenUtils {

    public static int getScreenWidth(Context context) {

        return context.getResources().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
