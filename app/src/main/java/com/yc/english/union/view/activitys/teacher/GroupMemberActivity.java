package com.yc.english.union.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupMyMemberListContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupMyMemberListPresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.view.adapter.GroupMemberAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;

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
    TextView tvShareGroup;
    private GroupMemberAdapter adapter;
    private SimpleHeaderAdapter<StudentInfo> simpleHeaderAdapter;

    @Override
    public void init() {
        mPresenter = new GroupMyMemberListPresenter(this, this);

        mToolbar.setTitle(GroupInfoHelper.getGroupInfo().getName());
        mToolbar.showNavigationIcon();

        if (GroupInfoHelper.getClassInfo() != null && GroupInfoHelper.getClassInfo().getMaster_id() != null) {
            if (GroupInfoHelper.getClassInfo().getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                mToolbar.setMenuTitle(getResources().getString(R.string.group_manager));
                tvShareGroup.setVisibility(View.GONE);
            } else {
                tvShareGroup.setVisibility(View.VISIBLE);
            }
        }

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupMemberActivity.this, GroupManagerActivity.class);
                intent.putExtra("group", GroupInfoHelper.getGroupInfo());
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOverlayStyle_Center();
        getData();
    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_class_manager;
    }

    @OnClick({R.id.tv_exit_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit_group:
                final AlertDialog alertDialog = new AlertDialog(this);
                alertDialog.setDesc("是否退出班群");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.exitGroup(GroupInfoHelper.getClassInfo().getClass_id(), GroupInfoHelper.getClassInfo().getMaster_id(), UserInfoHelper.getUserInfo().getUid());
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
        }
    }

    @Override
    public void showMemberList(List<StudentInfo> list) {
        if (simpleHeaderAdapter != null) {
            recyclerView.removeHeaderAdapter(simpleHeaderAdapter);
        }
        simpleHeaderAdapter = new SimpleHeaderAdapter<>(this.adapter, null, null, list.subList(0, 1));
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
