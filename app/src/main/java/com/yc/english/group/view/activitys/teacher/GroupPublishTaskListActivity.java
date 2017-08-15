package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupPublishTaskListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.model.bean.TaskInfos;
import com.yc.english.group.presenter.GroupPublishTaskListPresenter;
import com.yc.english.group.view.adapter.GroupTaskListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 08:57.
 */

public class GroupPublishTaskListActivity extends FullScreenActivity<GroupPublishTaskListPresenter> implements GroupPublishTaskListContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    private GroupTaskListAdapter adapter;
    private ClassInfo classInfo;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskListPresenter(this, this);
        if (getIntent() != null) {
            classInfo = getIntent().getParcelableExtra("classInfo");
            mPresenter.getPublishTaskList(classInfo.getMaster_id(), classInfo.getClass_id());
        }

        mToolbar.setTitle(getString(R.string.my_publish));
        mToolbar.setMenuTitle(getString(R.string.i_want_to_publish));
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupTaskListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        initListener();

    }


    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupPublishTaskListActivity.this, GroupIssueTaskActivity.class);
                intent.putExtra("targetId", classInfo.getClass_id());
                startActivity(intent);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_publish_task;
    }


    @Override
    public void showPublishTaskList(List<TaskAllInfoWrapper.TaskAllInfo> list) {
        adapter.setData(list);
    }
}
