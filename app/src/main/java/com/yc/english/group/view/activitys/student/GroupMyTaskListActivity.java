package com.yc.english.group.view.activitys.student;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupDoTaskListContract;
import com.yc.english.group.model.bean.TaskInfos;
import com.yc.english.group.presenter.GroupDoTaskListPresenter;
import com.yc.english.group.view.activitys.teacher.GroupIssueTaskActivity;
import com.yc.english.group.view.adapter.GroupMyTaskListAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 08:57.
 */

public class GroupMyTaskListActivity extends FullScreenActivity<GroupDoTaskListPresenter> implements GroupDoTaskListContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    private GroupMyTaskListAdapter adapter;

    @Override
    public void init() {
        mPresenter = new GroupDoTaskListPresenter(this, this);

        if (getIntent() != null) {

            String targetId = getIntent().getStringExtra("targetId");
            mPresenter.getDoTaskList(targetId, UserInfoHelper.getUserInfo().getUid());

        }
        mToolbar.setTitle(getString(R.string.task));
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMyTaskListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        initData();
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

    private void initData() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open("task.json");
            byte[] b = new byte[1024];
            int n;
            while ((n = is.read(b)) != -1) {
                sb.append(new String(b, 0, n));
            }

            TaskInfos taskInfo = JSON.parseObject(sb.toString(), TaskInfos.class);
            adapter.setData(taskInfo.getList());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_publish_task;
    }


}
