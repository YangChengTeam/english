package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupMyMemberListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupMyMemberListPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.adapter.GroupMemberAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

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
    private GroupMemberAdapter adapter;
    private GroupInfo groupInfo;
    private SimpleHeaderAdapter<StudentInfo> simpleHeaderAdapter;
    private ClassInfo mClassInfo;


    @Override
    public void init() {
        mPresenter = new GroupMyMemberListPresenter(this,this);
        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            mToolbar.setTitle(groupInfo.getName());
        }
        mToolbar.showNavigationIcon();


        mPresenter.queryGroupById(this, groupInfo.getId(), "");

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupMemberActivity.this, GroupManagerActivity.class);
                intent.putExtra("group", groupInfo);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this);
        recyclerView.setAdapter(adapter);

        mPresenter.getMemberList(this, groupInfo.getId(), "1", "");

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_class_manager;
    }

    @OnClick({R.id.tv_share_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_group:
//                SharePopupWindow sharePopupWindow = new SharePopupWindow(this);
//                sharePopupWindow.show(getWindow().getDecorView()
                final AlertDialog alertDialog = new AlertDialog(this);
                alertDialog.setDesc("是否退出班群");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.exitGroup(mClassInfo.getClass_id(), mClassInfo.getMaster_id(), UserInfoHelper.getUserInfo().getUid());
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
        simpleHeaderAdapter = new SimpleHeaderAdapter<>(this.adapter, "", "", list.subList(0, 1));
        recyclerView.addHeaderAdapter(simpleHeaderAdapter);
        list.remove(0);
        adapter.setDatas(list);

    }

    @Override
    public void showGroupInfo(ClassInfo classInfo) {
        mClassInfo = classInfo;
        if (classInfo != null && classInfo.getMaster_id() != null) {
            if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                mToolbar.setTitle(classInfo.getClassName());
                mToolbar.setMenuTitle(getResources().getString(R.string.group_manager));
                invalidateOptionsMenu();

            }
        }

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
        mPresenter.getMemberList(this, groupInfo.getId(), "1", "");
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String result) {
        mPresenter.queryGroupById(this, groupInfo.getId(), "");
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
                mPresenter.getMemberList(GroupMemberActivity.this, groupInfo.getId(), "1", "");
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

}
