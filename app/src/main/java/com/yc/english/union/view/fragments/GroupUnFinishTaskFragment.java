package com.yc.english.union.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.english.union.view.adapter.GroupTaskFinishedAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 15:28.
 */

public class GroupUnFinishTaskFragment extends BaseFragment {

    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    private GroupTaskFinishedAdapter adapter;


    @Override
    public void init() {

        if (getArguments() != null) {

            List<StudentFinishTaskInfo.ListBean.NoDoneListBean> noDone_list = getArguments().getParcelableArrayList("noDone_list");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new GroupTaskFinishedAdapter(getActivity(), noDone_list);
            mRecyclerView.setAdapter(adapter);
            BaseItemDecoration decoration = new BaseItemDecoration(getContext());
            mRecyclerView.addItemDecoration(decoration);
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_fragment_task;
    }


}
