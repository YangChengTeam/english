package com.xinqu.videoplayer.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * TinyHung@Outlook.com
 * 2018/3/14.
 * 带有按下效果的ImageView
 */

public class TouchFilterImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "TouchFilterImageView";

    private GestureDetector mGestureDetector;

    public TouchFilterImageView(Context context) {
        super(context);
    }

    public TouchFilterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context,mGestureDetectorListener);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null!=mGestureDetector){
            mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener mGestureDetectorListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.d(TAG,"onLongPress");
            TouchFilterImageView.this.clearColorFilter(); // 清除滤镜
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            TouchFilterImageView.this.setColorFilter(Color.parseColor("#33000000")); // 设置滤镜效果
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    };
}
