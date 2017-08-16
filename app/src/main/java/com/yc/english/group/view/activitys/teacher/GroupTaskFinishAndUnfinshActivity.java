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
import com.yc.english.group.utils.TaskUtil;
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
        mTvIssueTime.setText(taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " + taskInfo.getAdd_time());
        mLlTaskDetail.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskTypeIcon, taskInfo, mLlTaskDetail);
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
}
