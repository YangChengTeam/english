package com.yc.english.base.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yc.english.R;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class MainToolBar extends BaseToolBar {
    public MainToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_view_toolbar;
    }
}
