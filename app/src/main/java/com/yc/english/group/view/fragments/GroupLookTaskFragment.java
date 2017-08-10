package com.yc.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.bean.StudentTaskInfo;
import com.yc.english.group.model.bean.TaskFinishedInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.presenter.GroupPublishTaskDetailPresenter;
import com.yc.english.group.view.adapter.GroupTaskFinishedAdapter;
import com.yc.english.group.view.adapter.GroupTaskLookAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 15:28.
 */

public class GroupLookTaskFragment extends BaseFragment<GroupPublishTaskDetailPresenter> implements GroupPublishTaskDetailContract.View {

    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;

    private List<TaskFinishedInfo> list = new ArrayList<>();
    private GroupTaskLookAdapter adapter;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskDetailPresenter(getActivity(), this);
        if (getArguments() != null) {
            String taskDetailInfo = getArguments().getString("extra");
            TaskInfo taskInfo = JSONObject.parseObject(taskDetailInfo, TaskInfo.class);
            if (taskInfo.getClass_ids() != null)
                mPresenter.getIsReadTaskList(taskInfo.getClass_ids().get(0), taskInfo.getId());
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GroupTaskLookAdapter(getActivity(), null);
        mRecyclerView.setAdapter(adapter);
        BaseItemDecoration decoration = new BaseItemDecoration(getContext());
        mRecyclerView.addItemDecoration(decoration);
        initData();

    }

    private void initData() {
        list.add(new TaskFinishedInfo("艾同学", "", "2017-07-19 20:45:23"));
        list.add(new TaskFinishedInfo("曹同学", "", "2017-07-19 20:45:23"));
        list.add(new TaskFinishedInfo("程同学", "", "2017-07-19 20:45:23"));
        adapter.setData(list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_fragment_task;
    }


    @Override
    public void showPublishTaskDetail(TaskInfo stringResultInfo) {

    }

    @Override
    public void showIsReadMemberList(StudentTaskInfo.ListBean data) {

    }
}
