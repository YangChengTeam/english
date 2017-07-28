package com.yc.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.group.model.bean.TaskFinishedInfo;
import com.yc.english.group.view.adapter.GroupTaskFinishedAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 15:28.
 */

public class GroupFinishTaskFragment extends BaseFragment {

    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;

    private List<TaskFinishedInfo> list = new ArrayList<>();
    private GroupTaskFinishedAdapter adapter;

    @Override
    public void init() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new GroupTaskFinishedAdapter(getActivity(), null);
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
    public int getLayoutID() {
        return R.layout.group_fragment_task;
    }


}
