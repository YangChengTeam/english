package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.english.group.model.bean.StudentLookTaskInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupPublishTaskDetailPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.adapter.CommonAdapter;
import com.yc.english.group.view.adapter.GroupPageAdapter;
import com.yc.english.group.view.fragments.GroupLookTaskFragment;
import com.yc.english.group.view.fragments.GroupUnLookTaskFragment;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/7/28 12:55.
 * 已查阅 未查阅
 */

public class GroupPublishTaskLookAndUnLookActivity extends FullScreenActivity<GroupPublishTaskDetailPresenter> implements GroupPublishTaskDetailContract.View {
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
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private List<Fragment> fragments = new ArrayList<>();
    private GroupLookTaskFragment groupLookTaskFragment;
    private String taskDetailInfo;
    private GroupUnLookTaskFragment groupUnLookTaskFragment;
    private TaskInfo taskInfo;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskDetailPresenter(this, this);
        if (getIntent() != null) {
            taskDetailInfo = getIntent().getStringExtra("extra");
            taskInfo = JSONObject.parseObject(taskDetailInfo, TaskInfo.class);
            getData();

        }

        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));

        initListener();


    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupPublishTaskLookAndUnLookActivity.this, GroupPublishTaskListActivity.class);
                ClassInfo classInfo = new ClassInfo();
                classInfo.setMaster_id(taskInfo.getPublisher());
                classInfo.setClass_id(taskInfo.getClass_ids().get(0));
                intent.putExtra("classInfo", classInfo);
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

        mTvIssueTime.setText(taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " + taskInfo.getAdd_time());
        mLlTaskDetail.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskTypeIcon, taskInfo, mLlTaskDetail);


    }

    private int readList = 0;
    private int unReadList = 0;

    @Override
    public void showIsReadMemberList(StudentLookTaskInfo.ListBean data) {

        CommonNavigator commonNavigator = new CommonNavigator(this);

        ArrayList<StudentLookTaskInfo.ListBean.NoreadListBean> read_list = data.getRead_list();
        ArrayList<StudentLookTaskInfo.ListBean.NoreadListBean> noread_list = data.getNoread_list();
        if (read_list != null && read_list.size() > 0) {
            readList = read_list.size();
        }
        if (noread_list != null && noread_list.size() > 0) {
            unReadList = noread_list.size();
        }
        commonNavigator.setAdapter(new CommonAdapter(this, 2, mViewPager, readList, unReadList));


        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);


        Bundle lookBundle = new Bundle();
        Bundle unLookBundle = new Bundle();

        lookBundle.putParcelableArrayList("look_list", read_list);
        unLookBundle.putParcelableArrayList("unLook_list", noread_list);

        groupLookTaskFragment = new GroupLookTaskFragment();
        groupLookTaskFragment.setArguments(lookBundle);
        fragments.add(groupLookTaskFragment);

        groupUnLookTaskFragment = new GroupUnLookTaskFragment();
        groupUnLookTaskFragment.setArguments(unLookBundle);
        fragments.add(groupUnLookTaskFragment);

        mViewPager.setAdapter(new GroupPageAdapter(getSupportFragmentManager(), fragments));

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

    }

    @Override
    public void showIsFinishMemberList(StudentFinishTaskInfo.ListBean list) {

    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    private void getData() {
        mPresenter.getPublishTaskDetail(this, taskInfo.getId(), taskInfo.getClass_ids().get(0), UserInfoHelper.getUserInfo().getUid());
        mPresenter.getIsReadTaskList(taskInfo.getClass_ids().get(0), taskInfo.getId());
    }
}
