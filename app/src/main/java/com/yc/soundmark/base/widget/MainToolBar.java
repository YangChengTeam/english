package com.yc.soundmark.base.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.yc.english.R;


/**
 * Created by wanglin  on 2018/10/24 17:45.
 */
public class MainToolBar extends BaseToolBar {


    public MainToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_toolbar_view;
    }
}
