package com.yc.english.setting.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.setting.view.widgets.SettingItemView;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class SettingActivity extends FullScreenActivity {


    @BindView(R.id.si_cache)
    SettingItemView mCacheSettingItemView;

    @BindView(R.id.si_push)
    SettingItemView mPushSettingItemView;

    @BindView(R.id.si_play)
    SettingItemView mPlaySettingItemView;

    @Override
    public void init() {
        mToolbar.setTitle("设置");
        mToolbar.showNavigationIcon();

        mCacheSettingItemView.rightInfo();
        mPushSettingItemView.setIcon(R.mipmap.setting_off);
        mPlaySettingItemView.setIcon(R.mipmap.setting_on);
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_activity_setting;
    }
}
