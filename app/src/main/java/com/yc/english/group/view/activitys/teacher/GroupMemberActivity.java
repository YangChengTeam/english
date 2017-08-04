package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupMyGroupListContract;
import com.yc.english.group.contract.GroupMyMemberListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupListPresenter;
import com.yc.english.group.presenter.GroupMyGroupListPresenter;
import com.yc.english.group.presenter.GroupMyMemberListPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.adapter.GroupMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/26 14:41.
 */

public class GroupMemberActivity extends FullScreenActivity<GroupMyMemberListPresenter> implements GroupMyMemberListContract.View {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_share_group)
    TextView tvShareGroup;

    private List<GroupMemberInfo> mList = new ArrayList<>();
    private GroupMemberAdapter adapter;
    private GroupInfo groupInfo;


    @Override
    public void init() {
        mPresenter = new GroupMyMemberListPresenter(this);
        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            mToolbar.setTitle(groupInfo.getName());
        }
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getResources().getString(R.string.group_manager));

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupMemberActivity.this, GroupManagerActivity.class);
                intent.putExtra("group", groupInfo);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this, null);
        recyclerView.setAdapter(adapter);
        mPresenter.getMemberList(this, groupInfo.getId(), "1", "");
        initListener();

    }

    private void initListener() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_class_manager;
    }

    @OnClick({R.id.tv_share_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_group:
                SharePopupWindow sharePopupWindow = new SharePopupWindow(this);
                sharePopupWindow.show(getWindow().getDecorView());
                break;
        }
    }



    @Override
    public void showMemberList(List<StudentInfo> list) {
        adapter.setData(list);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.FINISH)
            }
    )
    public void getList(String group) {
        if (group.equals(BusAction.REMOVE_GROUP)){
            finish();
        }
    }
}
