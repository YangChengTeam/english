package com.yc.junior.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseFragment;
import com.yc.junior.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.junior.english.group.view.adapter.GroupTaskFinishedAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 15:28.
 */

public class GroupUnFinishTaskFragment extends BaseFragment {

    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;


    @Override
    public void init() {

        if (getArguments() != null) {

            List<StudentFinishTaskInfo.ListBean.NoDoneListBean> noDone_list = getArguments().getParcelableArrayList("noDone_list");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GroupTaskFinishedAdapter adapter = new GroupTaskFinishedAdapter(getActivity(), noDone_list);
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
