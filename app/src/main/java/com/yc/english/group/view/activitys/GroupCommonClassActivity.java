package com.yc.english.group.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.contract.GroupListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupCommonClassPresenter;
import com.yc.english.group.presenter.GroupListPresenter;
import com.yc.english.group.view.adapter.GroupGroupAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/8/1 18:16.
 */

public class GroupCommonClassActivity extends FullScreenActivity<GroupCommonClassPresenter> implements GroupCommonClassContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    private GroupGroupAdapter adapter;

    @Override
    public void init() {
        mPresenter = new GroupCommonClassPresenter(this, this);
        mToolbar.setTitle(getString(R.string.teacher_education));
        mToolbar.showNavigationIcon();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupGroupAdapter(this, null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_list_join;
    }


    @Override
    public void showCommonClassList(List<ClassInfo> list) {
        adapter.setData(list);
    }
}
