package com.yc.english.group.view.activitys.student;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/7/27 18:30.
 * 学生查看作业评分详情
 */

public class GroupTaskGradeActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.task_detail));
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_grade;
    }
}
