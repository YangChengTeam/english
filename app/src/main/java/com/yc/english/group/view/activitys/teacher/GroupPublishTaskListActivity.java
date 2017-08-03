package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.model.bean.TaskInfos;
import com.yc.english.group.view.adapter.GroupTaskListAdapter;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 08:57.
 */

public class GroupPublishTaskListActivity extends FullScreenActivity {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    private GroupTaskListAdapter adapter;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.my_publish));
        mToolbar.setMenuTitle(getString(R.string.i_want_to_publish));
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupTaskListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        initData();
        initListener();

    }


    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GroupPublishTaskListActivity.this, GroupIssueTaskActivity.class));
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
