package com.yc.english.setting.view.activitys;

import android.content.Intent;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by zhangkai on 2017/7/27.
 */

public class NameSettingActivity extends FullScreenActivity {
    @Override
    public void init() {
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("name");
        mToolbar.setTitle(name);
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_activity_name;
    }
}
