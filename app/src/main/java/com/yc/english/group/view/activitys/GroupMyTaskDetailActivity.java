package com.yc.english.group.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/7/27 18:01.
 */

public class GroupMyTaskDetailActivity extends FullScreenActivity {

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_detail;
    }
}
