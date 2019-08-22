package com.yc.english.setting.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

import yc.com.base.BasePresenter;

/**
 * Created by suns  on 2019/8/21 16:42.
 */
public class PrivacyActivity extends FullScreenActivity<BasePresenter> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy;
    }

    @Override
    public void init() {
        mToolbar.setTitle("隐私政策");
        mToolbar.showNavigationIcon();
    }
}
