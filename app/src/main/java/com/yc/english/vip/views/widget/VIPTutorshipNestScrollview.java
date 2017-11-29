package com.yc.english.vip.views.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

import com.yc.english.news.view.widget.NewsScrollView;

/**
 * Created by wanglin  on 2017/11/29 11:21.
 */

public class VIPTutorshipNestScrollview extends NestedScrollView {
    public VIPTutorshipNestScrollview(Context context) {
        super(context);
    }

    public VIPTutorshipNestScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    private NewsScrollView.onScrollChangeListener listener;

    public NewsScrollView.onScrollChangeListener getListener() {
        return listener;
    }

    public void setOnScrollChangeListener(NewsScrollView.onScrollChangeListener listener) {
        this.listener = listener;
    }
}
