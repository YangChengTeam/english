package com.yc.english.group.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.MainToolBar;
import com.yc.english.base.view.ToolbarFragment;
import com.yc.english.group.contract.GroupListContract;
import com.yc.english.group.model.engin.GroupListEngine;

import butterknife.BindView;


/**
 * Created by wanglin  on 2017/7/24 10:20.
 */

public class GroupListFragment extends ToolbarFragment {


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
        super.init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public boolean isInstallToolbar() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_fragment_class_list;
    }


}
