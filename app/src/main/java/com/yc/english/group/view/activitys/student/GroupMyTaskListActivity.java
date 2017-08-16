package com.yc.english.group.view.activitys.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupDoTaskListContract;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.presenter.GroupDoTaskListPresenter;
import com.yc.english.group.view.activitys.teacher.GroupIssueTaskActivity;
import com.yc.english.group.view.adapter.GroupMyTaskListAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/7/28 08:57.
 */

public class GroupMyTaskListActivity extends FullScreenActivity<GroupDoTaskListPresenter> implements GroupDoTaskListContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;
    private GroupMyTaskListAdapter adapter;
    private String targetId;

    @Override
    public void init() {
        mPresenter = new GroupDoTaskListPresenter(this, this);

        if (getIntent() != null) {
            targetId = getIntent().getStringExtra("targetId");
            mPresenter.getDoTaskList(targetId, UserInfoHelper.getUserInfo().getUid());

        }
        mToolbar.setTitle(getString(R.string.task));
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMyTaskListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        initListener();

    }


    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GroupMyTaskListActivity.this, GroupIssueTaskActivity.class));
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_publish_task;
    }


    @Override
    public void showDoneTaskResult(List<TaskAllInfoWrapper.TaskAllInfo> list) {
        adapter.setData(list);
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getDoTaskList(targetId, UserInfoHelper.getUserInfo().getUid());
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mRecyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(mRecyclerView);
    }
}
