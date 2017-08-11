package com.yc.english.group.view.activitys.student;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupDoneTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupDoneTaskDetailPresenter;
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

    @Override
    public void init() {
        mPresenter = new GroupDoneTaskDetailPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.task_detail));
        if (getIntent() != null) {
            String taskId = getIntent().getStringExtra("taskId");
            String classId = getIntent().getStringExtra("classId");
            String id = getIntent().getStringExtra("id");
            mPresenter.getDoneTaskDetail(this,id, UserInfoHelper.getUserInfo().getUid());
            mPresenter.getPublishTaskDetail(this, taskId, classId, "");

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_grade;
    }

    @Override
    public void showDoneTaskDetail(TaskInfo info) {

        setScore(info);
        setType(info);

    }

    @Override
    public void showPublishTaskDetail(TaskInfo info) {
        mTvIssueTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        int type = Integer.parseInt(info.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                mIvTaskIcon.setImageResource(R.mipmap.group36);
                publishMultifunctionLinearLayout.showTextView();
                publishMultifunctionLinearLayout.setText(info.getDesp());
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                mIvTaskIcon.setImageResource(R.mipmap.group40);
                publishMultifunctionLinearLayout.showPictureView();
                publishMultifunctionLinearLayout.setUriList(info.getBody().getImgs());
                break;
            case GroupConstant.TASK_TYPE_WORD:
                mIvTaskIcon.setImageResource(R.mipmap.group42);
                publishMultifunctionLinearLayout.showWordView();
//                publishMultifunctionLinearLayout.setFileInfos();

                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskIcon.setImageResource(R.mipmap.group38);
                publishMultifunctionLinearLayout.showVoiceView();
//                publishMultifunctionLinearLayout.setVoices();
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                mIvTaskIcon.setImageResource(R.mipmap.group44);
                publishMultifunctionLinearLayout.setText(info.getDesp());
                publishMultifunctionLinearLayout.showSynthesizeView();
                publishMultifunctionLinearLayout.setUriList(info.getBody().getImgs());
                break;

        }
    }

    private void setType(TaskInfo info) {
        int type = Integer.parseInt(info.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                doMultifunctionLinearLayout.setText(info.getDesp());
                doMultifunctionLinearLayout.showTextView();
                break;
            case GroupConstant.TASK_TYPE_PICTURE:

                doMultifunctionLinearLayout.showPictureView();
                doMultifunctionLinearLayout.setUriList(info.getBody().getImgs());
                break;
            case GroupConstant.TASK_TYPE_WORD:

                doMultifunctionLinearLayout.showVoiceView();
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskIcon.setImageResource(R.mipmap.group38);
                doMultifunctionLinearLayout.showVoiceView();
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:

                doMultifunctionLinearLayout.setText(info.getDesp());
                doMultifunctionLinearLayout.showSynthesizeView();
                doMultifunctionLinearLayout.setUriList(info.getBody().getImgs());

                break;

        }

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


}
