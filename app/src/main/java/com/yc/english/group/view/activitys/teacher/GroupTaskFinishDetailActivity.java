package com.yc.english.group.view.activitys.teacher;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/8/2 11:41.
 * 老师查看完成作业详情
 */

public class GroupTaskFinishDetailActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_finished_detail;
    }
}
