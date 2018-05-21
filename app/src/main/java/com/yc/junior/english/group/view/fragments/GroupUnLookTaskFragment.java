package com.yc.junior.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseFragment;
import com.yc.junior.english.group.model.bean.StudentLookTaskInfo;
import com.yc.junior.english.group.view.adapter.GroupTaskLookAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 15:28.
 */

public class GroupUnLookTaskFragment extends BaseFragment {

    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public void init() {

        if (getArguments() != null) {
            List<StudentLookTaskInfo.ListBean.NoreadListBean> noRead_list = getArguments().getParcelableArrayList("unLook_list");

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GroupTaskLookAdapter adapter = new GroupTaskLookAdapter(getActivity(), noRead_list);
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
