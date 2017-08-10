package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.TimeUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.bean.StudentTaskInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupPublishTaskDetailPresenter;
import com.yc.english.group.view.adapter.CommonAdapter;
import com.yc.english.group.view.adapter.GroupPageAdapter;
import com.yc.english.group.view.fragments.GroupLookTaskFragment;
import com.yc.english.group.view.fragments.GroupUnLookTaskFragment;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.model.FileInfo;

/**
 * Created by wanglin  on 2017/7/28 12:55.
 * 已查阅 未查阅
 */

public class GroupTaskLookAndUnLookActivity extends FullScreenActivity<GroupPublishTaskDetailPresenter> implements GroupPublishTaskDetailContract.View {
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_ll_task_detail)
    MultifunctionLinearLayout mLlTaskDetail;
    @BindView(R.id.m_magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.m_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.m_iv_task_type_icon)
    ImageView mIvTaskTypeIcon;
    private List<Fragment> fragments = new ArrayList<>();
    private GroupLookTaskFragment groupLookTaskFragment;
    private String taskDetailInfo;
    private GroupUnLookTaskFragment groupUnLookTaskFragment;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskDetailPresenter(this, this);
        if (getIntent() != null) {
            taskDetailInfo = getIntent().getStringExtra("extra");
            TaskInfo taskInfo = JSONObject.parseObject(taskDetailInfo, TaskInfo.class);
            mPresenter.getPublishTaskDetail(this,taskInfo.getId(), taskInfo.getClass_ids().get(0), "");
            mPresenter.getIsReadTaskList(taskInfo.getClass_ids().get(0), taskInfo.getId());
        }

        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonAdapter(this, 2, mViewPager, 3, 17));

        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);

        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(taskDetailInfo)) bundle.putString("extra", taskDetailInfo);

        groupLookTaskFragment = new GroupLookTaskFragment();
        groupLookTaskFragment.setArguments(bundle);
        fragments.add(groupLookTaskFragment);

        groupUnLookTaskFragment = new GroupUnLookTaskFragment();
        groupUnLookTaskFragment.setArguments(bundle);
        fragments.add(groupUnLookTaskFragment);

        mViewPager.setAdapter(new GroupPageAdapter(getSupportFragmentManager(), fragments));

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        initListener();


    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupTaskLookAndUnLookActivity.this, GroupPublishTaskListActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_item_detail;
    }


    @Override
    public void showPublishTaskDetail(TaskInfo taskInfo) {

        setTaskType(taskInfo);
        mTvIssueTime.setText(taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " +
                TimeUtils.date2String(TimeUtils.millis2Date(Long.parseLong(taskInfo.getAdd_time())), new SimpleDateFormat("HH:mm:ss")));

    }

    @Override
    public void showIsReadMemberList(StudentTaskInfo.ListBean data) {
        data.getNoread_list();

    }

    private void setTaskType(TaskInfo taskInfo) {
        int type = Integer.parseInt(taskInfo.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group36));
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showTextView();
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group40));
                mLlTaskDetail.showPictureView();
                mLlTaskDetail.setUriList(getPictures(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group38));
                mLlTaskDetail.showVoiceView();

                mLlTaskDetail.setVoices(getVoiceList(taskInfo));

                break;
            case GroupConstant.TASK_TYPE_WORD:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group42));
                mLlTaskDetail.showWordView();

                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group44));
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showSynthesizeView();
                mLlTaskDetail.setUriList(getPictures(taskInfo));
                mLlTaskDetail.setVoices(getVoiceList(taskInfo));
                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;
        }
    }


    private List<Uri> getPictures(TaskInfo taskInfo) {
        List<String> imgs = taskInfo.getBody().getImgs();
        List<Uri> uriList = new ArrayList<>();
        if (imgs != null && imgs.size() > 0) {
            for (String img : imgs) {
                uriList.add(Uri.parse(img));
            }
        }
        return uriList;
    }

    private List<Voice> getVoiceList(TaskInfo taskInfo) {
        List<String> voice = taskInfo.getBody().getVoices();
        List<Voice> voiceList = new ArrayList<>();

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if (voice != null && voice.size() > 0) {
                for (String s : voice) {
                    mediaPlayer.setDataSource(s);
                    mediaPlayer.prepare();
                    int duration = mediaPlayer.getDuration();
                    int second = duration / 1000;
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
