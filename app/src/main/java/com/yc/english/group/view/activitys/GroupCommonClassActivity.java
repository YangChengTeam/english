package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.contract.GroupListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupCommonClassPresenter;
import com.yc.english.group.presenter.GroupListPresenter;
import com.yc.english.group.view.activitys.student.GroupJoinActivity;
import com.yc.english.group.view.adapter.GroupGroupAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

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
        adapter = new GroupGroupAdapter(this, false, null);
        recyclerView.setAdapter(adapter);
        initListener();

    }

    private void initListener() {
        RxView.clicks(btnJoinClass).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(GroupCommonClassActivity.this))
                    startActivity(new Intent(GroupCommonClassActivity.this, GroupJoinActivity.class));
            }
        });

        adapter.setOnJoinListener(new GroupGroupAdapter.OnJoinListener() {
            @Override
            public void onJoin(ClassInfo classInfo) {

                mPresenter.applyJoinGroup(UserInfoHelper.getUserInfo().getUid(), classInfo.getGroupId() + "");
            }
        });
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
