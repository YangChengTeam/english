package com.yc.english.setting.view.fragments;

import com.yc.english.R;
import com.yc.english.base.view.ToolbarFragment;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class MyFragment extends ToolbarFragment {
    @Override
    public void init() {
        super.init();
        mToolbar.setTitle("我的");
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_fragment_my;
    }
}
