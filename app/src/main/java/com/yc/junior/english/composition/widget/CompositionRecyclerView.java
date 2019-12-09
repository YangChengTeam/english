package com.yc.junior.english.composition.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
