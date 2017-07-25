package com.yc.english.group.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_create_class;
    }
}
