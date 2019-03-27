package com.yc.english.composition.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wanglin  on 2019/3/26 15:32.
 */
public class CompositionRecyclerView extends RecyclerView {
    public CompositionRecyclerView(Context context) {
        super(context);

    }

    public CompositionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public CompositionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if (isMove) return true;
        }

        return false;
    }


}
