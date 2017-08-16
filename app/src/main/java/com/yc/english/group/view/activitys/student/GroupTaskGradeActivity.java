package com.yc.english.group.view.activitys.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupDoneTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupDoneTaskDetailPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/7/27 18:30.
 * 学生查看作业评分详情
 */

public class GroupTaskGradeActivity extends FullScreenActivity<GroupDoneTaskDetailPresenter> implements GroupDoneTaskDetailContract.View {
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_iv_grade)
    ImageView mIvGrade;

    @BindView(R.id.m_iv_task_icon)
    ImageView mIvTaskIcon;
    @BindView(R.id.publish_multifunctionLinearLayout)
    MultifunctionLinearLayout publishMultifunctionLinearLayout;
    @BindView(R.id.do_multifunctionLinearLayout)
    MultifunctionLinearLayout doMultifunctionLinearLayout;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.sl_container)
    ScrollView slContainer;
    private TaskInfo taskInfo;

    @Override
    public void init() {
        mPresenter = new GroupDoneTaskDetailPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.task_detail));
        if (getIntent() != null) {
            if (getIntent().getStringExtra("extra") != null) {
                String detailTask = getIntent().getStringExtra("extra");
                taskInfo = JSONObject.parseObject(detailTask, TaskInfo.class);
                getData();
            } else {
                String taskId = getIntent().getStringExtra("taskId");
                String classId = getIntent().getStringExtra("classId");
                String id = getIntent().getStringExtra("id");
                mPresenter.getDoneTaskDetail(this, id, UserInfoHelper.getUserInfo().getUid());
                mPresenter.getPublishTaskDetail(this, taskId, classId, UserInfoHelper.getUserInfo().getUid());
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_grade;
    }

    @Override
    public void showDoneTaskDetail(TaskInfo info) {
        doMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.DONE);
        TaskUtil.showContextView(mIvTaskIcon, info, doMultifunctionLinearLayout);
        setScore(info);

    }

    @Override
    public void showPublishTaskDetail(TaskInfo info) {
        mTvIssueTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        publishMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskIcon, info, publishMultifunctionLinearLayout);
    }

    private void setScore(TaskInfo info) {
        if (info.getScore() != null) {
            if (info.getScore().equals("A+")) {
                mIvGrade.setImageResource(R.mipmap.group30);
            } else if (info.getScore().equals("A")) {
                mIvGrade.setImageResource(R.mipmap.group31);
            } else if (info.getScore().equals("B+")) {
                mIvGrade.setImageResource(R.mipmap.group32);
            } else if (info.getScore().equals("B")) {
                mIvGrade.setImageResource(R.mipmap.group33);
            } else if (info.getScore().equals("C")) {
                mIvGrade.setImageResource(R.mipmap.group34);
            }
        }
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(slContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(slContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(slContainer);
    }

    private void getData() {
        mPresenter.getDoneTaskDetail(this, taskInfo.getId(), UserInfoHelper.getUserInfo().getUid());
        mPresenter.getPublishTaskDetail(this, taskInfo.getTask_id(), taskInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid());
    }
}
