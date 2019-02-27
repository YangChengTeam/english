package com.yc.soundmark.base.utils;

import android.content.Context;
import android.view.View;

/**
 * Created by wanglin  on 2018/11/7 15:27.
 */
public class UIUtils {

    private static int bottom_bar_height = 0;
    private static int top_bar_height = 0;

    private static final int[] location = new int[2];


    private static UIUtils instance;
    private Context mContext;

    private UIUtils(Context context) {
        this.mContext = context;

    }


    public static UIUtils getInstance(Context context) {
        synchronized (UIUtils.class) {
            if (instance == null) {
                synchronized (UIUtils.class) {
                    instance = new UIUtils(context);
                }
            }
        }

        return instance;

    }


    public void measureBottomBarHeight(final View view) {
        if (view == null) return;

        view.post(new Runnable() {
            @Override
            public void run() {
                bottom_bar_height = view.getBottom() - view.getTop();
            }
        });
    }

    public int getBottomBarHeight() {

        return bottom_bar_height;
    }


    public void measureViewLoction(final View view) {

        if (view == null) return;
        view.post(new Runnable() {
            @Override
            public void run() {
                view.getLocationInWindow(location);

                int top = view.getTop();
                int bottom = view.getBottom();
                location[1] = location[1] + bottom - top;


            }
        });


    }

    public int[] getLocation() {
        return location;
    }
}
