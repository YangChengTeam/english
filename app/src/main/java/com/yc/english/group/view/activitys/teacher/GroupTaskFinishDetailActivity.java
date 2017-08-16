package com.yc.english.group.view.activitys.teacher;

import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupScoreTaskContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupScoreTaskPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.model.FileInfo;

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
            if (getIntent().getStringExtra("extra") != null) {
                String taskDetail = getIntent().getStringExtra("extra");
                TaskInfo taskInfo = JSONObject.parseObject(taskDetail, TaskInfo.class);
                doneId = taskInfo.getId();
                mPresenter.getPublishTaskDetail(this, taskInfo.getTask_id(), taskInfo.getClass_id(), "");
                mPresenter.getDoneTaskDetail(this, taskInfo.getId(), taskInfo.getUser_id());
            } else {
                String taskId = getIntent().getStringExtra("mTaskId");
                String classId = getIntent().getStringExtra("mClassId");
                String userId = getIntent().getStringExtra("userId");
                doneId = getIntent().getStringExtra("doneId");
                mPresenter.getPublishTaskDetail(this, taskId, classId, "");
                mPresenter.getDoneTaskDetail(this, doneId, userId);
            }
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
        if (TextUtils.isEmpty(info.getScore()))
            initListener();
    }

    @Override
    public void showPublishTaskInfo(TaskInfo info) {
        mTvTaskTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        publishMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskIcon, info, publishMultifunctionLinearLayout);
    }

    @Override
    public void showScoreResult() {

        TipsHelper.tips(this, "您已经为该做业进行打分");

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

    private List<Voice> getVoiceList(TaskInfo taskInfo) {
        List<String> voice = taskInfo.getBody().getVoices();
        List<Voice> voiceList = new ArrayList<>();
        try {
            if (voice != null && voice.size() > 0) {
                for (String s : voice) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(s);
                    mediaPlayer.prepare();
                    int duration = mediaPlayer.getDuration();
                    int second = duration / 1000;
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    voiceList.add(new Voice(s, second + "''"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voiceList;
    }


    private List<FileInfo> getFileInfos(TaskInfo taskInfo) {

        List<String> list = taskInfo.getBody().getDocs();
        List<FileInfo> fileInfos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String s : list) {

                FileInfo fileInfo = new FileInfo();
                fileInfo.setFilePath(s);
                fileInfo.setFileName(s.substring(s.lastIndexOf("/") + 1));
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

}
