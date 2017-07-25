package com.yc.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.MainToolBar;

import butterknife.BindView;



/**
 * Created by wanglin  on 2017/7/24 10:20.
 */

public class ClassListFragment extends BaseFragment {


    @BindView(R.id.toolbar)
    MainToolBar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;


    @Override
    public void init() {
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public int getLayoutID() {
        return R.layout.group_fragment_class_list;
    }


}
