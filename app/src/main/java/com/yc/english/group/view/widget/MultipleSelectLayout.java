package com.yc.english.group.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.yc.english.R;

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
        View view = View.inflate(context, R.layout.group_delete_member_item, null);
        addView(view);
        initView(view);


    }

    private void initView(View view) {

         view.findViewById(R.id.ll_container).setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
    }


}
