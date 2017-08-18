package com.yc.english.group.view.activitys.teacher;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupScoreTaskContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupScoreTaskPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/8/2 11:41.
 * 老师查看完成作业详情
 */

public class GroupTaskFinishDetailActivity extends FullScreenActivity<GroupScoreTaskPresenter> implements GroupScoreTaskContract.View {


    @BindView(R.id.m_iv_task_icon)
    ImageView mIvTaskIcon;
    @BindView(R.id.m_tv_task_time)
    TextView mTvTaskTime;
    @BindView(R.id.publish_multifunctionLinearLayout)
    MultifunctionLinearLayout publishMultifunctionLinearLayout;
    @BindView(R.id.do_multifunctionLinearLayout)
    MultifunctionLinearLayout doMultifunctionLinearLayout;
    @BindView(R.id.m_rb_a_extra)
    RadioButton mRbAExtra;
    @BindView(R.id.m_rb_a)
    RadioButton mRbA;
    @BindView(R.id.m_rb_b_extra)
    RadioButton mRbBExtra;
    @BindView(R.id.m_rb_b)
    RadioButton mRbB;
    @BindView(R.id.m_rb_c)
    RadioButton mRbC;
    @BindView(R.id.m_rg_container)
    RadioGroup mRgContainer;
    String doneId;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.sl_container)
    ScrollView slContainer;
    private TaskInfo taskInfo;
    private String taskId;
    private String classId;
    private String userId;

    @Override
    public void init() {
        mPresenter = new GroupScoreTaskPresenter(this, this);
        if (getIntent() != null) {
            if (getIntent().getStringExtra("extra") != null) {
                String taskDetail = getIntent().getStringExtra("extra");
                taskInfo = JSONObject.parseObject(taskDetail, TaskInfo.class);
                doneId = taskInfo.getId();

            } else {
                taskId = getIntent().getStringExtra("taskId");
                classId = getIntent().getStringExtra("classId");
                userId = getIntent().getStringExtra("userId");
                doneId = getIntent().getStringExtra("doneId");
            }
            getData();
        }

        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
    }

    private String score;

    private void initListener() {
        mRgContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.m_rb_a_extra:
                        score = "A+";
                        break;
                    case R.id.m_rb_a:
                        score = "A";
                        break;
                    case R.id.m_rb_b_extra:
                        score = "B+";
                        break;
                    case R.id.m_rb_b:
                        score = "B";
                        break;
                    case R.id.m_rb_c:
                        score = "C";
                        break;

                }
                mPresenter.taskScore(GroupTaskFinishDetailActivity.this, doneId, score);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_finished_detail;
    }

    @Override
    public void showDoneTaskInfo(TaskInfo info) {
        setScore(info);
        doMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.DONE);
        TaskUtil.showContextView(mIvTaskIcon, info, doMultifunctionLinearLayout);
        if (TextUtils.isEmpty(info.getScore())) {
            initListener();
        } else {
            mRbAExtra.setEnabled(false);
            mRbA.setEnabled(false);
            mRbBExtra.setEnabled(false);
            mRbB.setEnabled(false);
            mRbC.setEnabled(false);
        }
    }

    @Override
    public void showPublishTaskInfo(TaskInfo info) {
        mTvTaskTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        publishMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.PUBLISH);
        TaskUtil.showContextView(mIvTaskIcon, info, publishMultifunctionLinearLayout);
    }

    @Override
    public void showScoreResult() {

        TipsHelper.tips(this, "作业打分成功");

    }

    private void setScore(TaskInfo taskInfo) {
        switch (taskInfo.getScore()) {
            case "A+":
                mRbAExtra.setChecked(true);
                break;
            case "A":
                mRbA.setChecked(true);
                break;
            case "B+":
                mRbBExtra.setChecked(true);
                break;
            case "B":
                mRbB.setChecked(true);
                break;
            case "C":
                mRbC.setChecked(true);
                break;
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
        if (taskInfo != null) {
            mPresenter.getPublishTaskDetail(this, taskInfo.getTask_id(), taskInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid());
            mPresenter.getDoneTaskDetail(this, taskInfo.getId(), taskInfo.getUser_id());
        } else {
            mPresenter.getPublishTaskDetail(this, taskId, classId, UserInfoHelper.getUserInfo().getUid());
            mPresenter.getDoneTaskDetail(this, doneId, userId);
        }
    }
}
