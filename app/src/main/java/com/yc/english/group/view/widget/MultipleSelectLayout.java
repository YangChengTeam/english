package com.yc.english.group.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wanglin  on 2017/8/3 18:14.
 */

public class MultipleSelectLayout extends LinearLayout {
    public MultipleSelectLayout(Context context) {
        this(context, null);
    }

    public MultipleSelectLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return true;

    }
}
