package com.yc.junior.english.base.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yc.english.R;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class TransparentToolBar extends BaseToolBar {

    public TransparentToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public int getLayoutId() {
        return R.layout.base_tansparent_toolbar;
    }


    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }
}
