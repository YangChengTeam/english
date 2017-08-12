package com.yc.english.group.view.activitys.teacher;

import android.support.annotation.IdRes;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupScoreTaskContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupScoreTaskPresenter;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;

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

    @Override
    public void init() {
        mPresenter = new GroupScoreTaskPresenter(this, this);
        if (getIntent() != null) {
            String taskId = getIntent().getStringExtra("mTaskId");
            String classId = getIntent().getStringExtra("mClassId");
            String userId = getIntent().getStringExtra("userId");
            doneId = getIntent().getStringExtra("doneId");
            mPresenter.getPublishTaskDetail(this, taskId, classId, "");
            mPresenter.getDoneTaskDetail(this, doneId, userId);
        }

        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        initListener();

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
                mPresenter.taskScore(GroupTaskFinishDetailActivity.this ,doneId, score);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_finished_detail;
    }

    @Override
    public void showDoneTaskInfo(TaskInfo info) {
        setTaskDetail(info, doMultifunctionLinearLayout);
    }

    @Override
    public void showPublishTaskInfo(TaskInfo info) {
        mTvTaskTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        setTaskDetail(info, publishMultifunctionLinearLayout);

    }

    private void setTaskDetail(TaskInfo info, MultifunctionLinearLayout multifunctionLinearLayout) {
        int type = Integer.parseInt(info.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                mIvTaskIcon.setImageResource(R.mipmap.group36);
                multifunctionLinearLayout.setText(info.getDesp());
                multifunctionLinearLayout.showTextView();
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                mIvTaskIcon.setImageResource(R.mipmap.group40);
                multifunctionLinearLayout.showPictureView();
                multifunctionLinearLayout.setUriList(info.getBody().getImgs());
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskIcon.setImageResource(R.mipmap.group38);
                multifunctionLinearLayout.showVoiceView();
//                multifunctionLinearLayout.setVoices();
                break;
            case GroupConstant.TASK_TYPE_WORD:
                mIvTaskIcon.setImageResource(R.mipmap.group42);
                multifunctionLinearLayout.showWordView();
//                multifunctionLinearLayout.setFileInfos();
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                mIvTaskIcon.setImageResource(R.mipmap.group44);
                multifunctionLinearLayout.setText(info.getDesp());
                multifunctionLinearLayout.showSynthesizeView();
                multifunctionLinearLayout.setUriList(info.getBody().getImgs());
                break;
        }

    }

}
