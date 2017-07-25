package com.yc.english.group.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class GroupJoinActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_join_class;
    }
}
