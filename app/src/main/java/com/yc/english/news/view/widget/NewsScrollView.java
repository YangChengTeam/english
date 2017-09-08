package com.yc.english.news.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by wanglin  on 2017/9/7 15:38.
 */

public class NewsScrollView extends ScrollView {

    private int scaledTouchSlop;
    private int y;


    public NewsScrollView(Context context) {
        this(context, null);
    }

    public NewsScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NewsScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(((int) ev.getY()) - y) > scaledTouchSlop) {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScrollChange(l, t, oldl, oldt);
        }

    }

    public interface onScrollChangeListener {
        void onScrollChange(int l, int t, int oldl, int oldt);
    }

    private onScrollChangeListener listener;

    public onScrollChangeListener getListener() {
        return listener;
    }

    public void setListener(onScrollChangeListener listener) {
        this.listener = listener;
    }
}
