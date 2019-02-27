package com.yc.soundmark.study.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kk.utils.LogUtil;

/**
 * Created by wanglin  on 2018/10/26 14:38.
 */
public class StudyViewPager extends ViewPager {


    private static final String TAG = "StudyViewPager";
    private int startX;
    private int startY;
    private int curPosition;

    public StudyViewPager(Context context) {
        super(context);
    }

    public StudyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:
                curPosition = this.getCurrentItem();
                int count = this.getAdapter().getCount();

                float upX = ev.getX();
                float upY = ev.getY();

                float x = Math.abs(upX - startX);
                float y = Math.abs(upY - startY);
                double z = Math.sqrt(x * x + y * y);
                int degree = Math.round((float) (Math.asin(y / z) / Math.PI * 180));// 角度

//                LogUtil.msg("deltx: " + Math.abs(ev.getX() - startX) + " delty:   " + Math.abs(ev.getY() - startY));
                if (upX < startX && degree <= 45 || (upX > startX && degree <= 45)) {//这是我的滑动事件，父控件不要拦截
                    Log.i(TAG, "curPosition:=" + curPosition);
                    // 当当前页面在最后一页和第0页的时候，由父亲拦截触摸事件
                    if (curPosition == count - 1 || curPosition == 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {//其他情况，由孩子拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

        }
        return super.dispatchTouchEvent(ev);
    }


}
