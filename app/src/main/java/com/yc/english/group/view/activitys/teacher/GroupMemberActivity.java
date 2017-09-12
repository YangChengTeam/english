package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupMyMemberListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupMyMemberListPresenter;
import com.yc.english.group.view.adapter.GroupMemberAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/26 14:41.
 */

public class GroupMemberActivity extends FullScreenActivity<GroupMyMemberListPresenter> implements GroupMyMemberListContract.View {


    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.recyclerView)
    IndexableLayout recyclerView;

    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.tv_exit_group)
    TextView tvExitGroup;

    private GroupMemberAdapter adapter;
    private SimpleHeaderAdapter<StudentInfo> simpleHeaderAdapter;

    private String exitGroup = "";

    @Override
    public void init() {
        mPresenter = new GroupMyMemberListPresenter(this, this);

        mToolbar.setTitle(GroupInfoHelper.getGroupInfo().getName());
        mToolbar.showNavigationIcon();


        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupMemberActivity.this, GroupManagerActivity.class);
                intent.putExtra("group", GroupInfoHelper.getGroupInfo());
                startActivity(intent);
            }
        });

        ClassInfo classInfo = GroupInfoHelper.getClassInfo();
        if (classInfo != null && classInfo.getMaster_id() != null) {
            if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                if (classInfo.getType().equals("1")) {
                    mToolbar.setMenuTitle(getResources().getString(R.string.union_manager));
                } else {
                    mToolbar.setMenuTitle(getResources().getString(R.string.group_manager));
                }
                tvExitGroup.setVisibility(View.GONE);
            } else {
                tvExitGroup.setVisibility(View.VISIBLE);
                if (classInfo.getType().equals("1")) {
                    exitGroup = getResources().getString(R.string.exit_union);
                } else {
                    exitGroup = getResources().getString(R.string.exit_group);

                }
                tvExitGroup.setText(exitGroup);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOverlayStyle_Center();

        getData();

        initListener();

    }

    private void initListener() {
        RxView.clicks(tvExitGroup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                final AlertDialog alertDialog = new AlertDialog(GroupMemberActivity.this);
                alertDialog.setDesc("是否" + exitGroup);
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.exitGroup(GroupInfoHelper.getClassInfo().getClass_id(), GroupInfoHelper.getClassInfo().getMaster_id(), UserInfoHelper.getUserInfo().getUid());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_class_manager;
    }


    @Override
    public void showMemberList(List<StudentInfo> list) {
        if (simpleHeaderAdapter != null) {
            recyclerView.removeHeaderAdapter(simpleHeaderAdapter);
        }

        simpleHeaderAdapter = new SimpleHeaderAdapter<>(adapter, null, null, list.subList(0, 1));

        recyclerView.addHeaderAdapter(simpleHeaderAdapter);
        list.remove(0);
        adapter.setDatas(list);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.FINISH)
            }
    )
    public void getList(String group) {
        if (group.equals(BusAction.REMOVE_GROUP)) {
            finish();
        }

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DELETE_MEMBER)
            }
    )
    public void getMemberList(String group) {
        getData();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String result) {
        mToolbar.setTitle(result);
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    private void getData() {
        mPresenter.getMemberList(this, GroupInfoHelper.getGroupInfo().getId(), "1", "", GroupInfoHelper.getClassInfo().getFlag());
    }

}
