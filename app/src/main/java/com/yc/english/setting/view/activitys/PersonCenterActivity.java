package com.yc.english.setting.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class PersonCenterActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.setTitle("个人中心");
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_person_center_activity;
    }
}
