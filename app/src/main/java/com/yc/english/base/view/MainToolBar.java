package com.yc.english.base.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.yc.english.R;

import butterknife.BindView;

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
