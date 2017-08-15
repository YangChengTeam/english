package com.yc.english.group.view.activitys.teacher;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.english.group.model.bean.StudentLookTaskInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupPublishTaskDetailPresenter;
import com.yc.english.group.view.adapter.CommonAdapter;
import com.yc.english.group.view.adapter.GroupPageAdapter;
import com.yc.english.group.view.fragments.GroupFinishTaskFragment;
import com.yc.english.group.view.fragments.GroupUnFinishTaskFragment;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.model.FileInfo;

/**
 * Created by wanglin  on 2017/7/28 12:55.
 * 已完成 未完成
 */

public class GroupTaskFinishAndUnfinshActivity extends FullScreenActivity<GroupPublishTaskDetailPresenter> implements GroupPublishTaskDetailContract.View {

    @BindView(R.id.m_iv_task_type_icon)
    ImageView mIvTaskTypeIcon;
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_ll_task_detail)
    MultifunctionLinearLayout mLlTaskDetail;
    @BindView(R.id.m_magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.m_viewPager)
    ViewPager mViewPager;
    private String masterId;
    private List<Fragment> fragments = new ArrayList<>();
    private String taskId;
    private String classId;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskDetailPresenter(this, this);
        if (getIntent() != null) {
            taskId = getIntent().getStringExtra("taskId");
            classId = getIntent().getStringExtra("classId");
            masterId = getIntent().getStringExtra("masterId");
            mPresenter.getPublishTaskDetail(this, taskId, classId, "");
            mPresenter.getIsFinishTaskList(classId, taskId);
        }

        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();


    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_item_detail;
    }


    @Override
    public void showPublishTaskDetail(TaskInfo taskInfo) {
        setType(taskInfo);
        mTvIssueTime.setText(taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " + taskInfo.getAdd_time());

    }

    private void setType(TaskInfo taskInfo) {
        int type = Integer.parseInt(taskInfo.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                mIvTaskTypeIcon.setImageResource(R.mipmap.group36);
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showTextView();
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                mIvTaskTypeIcon.setImageResource(R.mipmap.group40);
                mLlTaskDetail.showPictureView();
                mLlTaskDetail.setUriList(taskInfo.getBody().getImgs());
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskTypeIcon.setImageResource(R.mipmap.group38);
                mLlTaskDetail.showVoiceView();
                mLlTaskDetail.setVoices(getVoiceList(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_WORD:
                mIvTaskTypeIcon.setImageResource(R.mipmap.group42);
                mLlTaskDetail.showWordView();
                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                mIvTaskTypeIcon.setImageResource(R.mipmap.group44);
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showSynthesizeView();
                mLlTaskDetail.setUriList(taskInfo.getBody().getImgs());
                mLlTaskDetail.setVoices(getVoiceList(taskInfo));
                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;


        }
    }

    @Override
    public void showIsReadMemberList(StudentLookTaskInfo.ListBean data) {

    }

    private int noDone_List;
    private int done_List;

    @Override
    public void showIsFinishMemberList(StudentFinishTaskInfo.ListBean list) {
        ArrayList<StudentFinishTaskInfo.ListBean.NoDoneListBean> done_list = list.getDone_list();
        ArrayList<StudentFinishTaskInfo.ListBean.NoDoneListBean> nodone_list = list.getNodone_list();
        if (nodone_list != null && nodone_list.size() > 0) {
            noDone_List = nodone_list.size();
        }
        if (done_list != null && done_list.size() > 0) {
            done_List = done_list.size();
        }

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonAdapter(this, 1, mViewPager, done_List, noDone_List));
        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);

        Bundle doneBundle = new Bundle();
        Bundle noDoneBundle = new Bundle();
        doneBundle.putParcelableArrayList("done_list", done_list);
        doneBundle.putString("taskId", taskId);
        doneBundle.putString("classId", classId);
        doneBundle.putString("masterId", masterId);

        noDoneBundle.putParcelableArrayList("noDone_list", nodone_list);

        GroupFinishTaskFragment groupFinishTaskFragment = new GroupFinishTaskFragment();
        groupFinishTaskFragment.setArguments(doneBundle);

        GroupUnFinishTaskFragment groupUnFinishTaskFragment = new GroupUnFinishTaskFragment();
        groupUnFinishTaskFragment.setArguments(noDoneBundle);

        fragments.add(groupFinishTaskFragment);
        fragments.add(groupUnFinishTaskFragment);

        mViewPager.setAdapter(new GroupPageAdapter(getSupportFragmentManager(), fragments));

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
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
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

}
