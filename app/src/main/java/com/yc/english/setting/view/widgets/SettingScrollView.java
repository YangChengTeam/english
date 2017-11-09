package com.yc.english.setting.view.widgets;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by wanglin  on 2017/11/8 15:28.
 */

public class SettingScrollView extends ScrollView {
    public SettingScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action =ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                int downY= (int) ev.getY();
                break;
        }


        return super.onInterceptTouchEvent(ev);

    }
}
