package com.yc.english.group.view.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.ToolbarFragment;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupMyGroupListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupMyGroupListPresenter;
import com.yc.english.group.view.activitys.student.GroupJoinActivity;
import com.yc.english.group.view.activitys.teacher.GroupCreateActivity;
import com.yc.english.group.view.activitys.teacher.GroupVerifyActivity;
import com.yc.english.group.view.adapter.GroupGroupAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class GroupMainFragment extends ToolbarFragment<GroupMyGroupListPresenter> implements GroupMyGroupListContract.View {
    private static final String TAG = "GroupMainFragment";

    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty_container)
    LinearLayout llEmptyContainer;
    @BindView(R.id.ll_data_container)
    LinearLayout llDataContainer;

    private GroupGroupAdapter adapter;


    @Override
    public void init() {
        super.init();
        mPresenter = new GroupMyGroupListPresenter(getActivity(), this);
        mToolbar.setTitle(getString(R.string.group));

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(getActivity(), GroupVerifyActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupGroupAdapter(getContext(), null);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean isInstallToolbar() {
        return true;
    }

    @Override
    public int getLayoutId() {

        return R.layout.group_fragment_list_main;

    }

    @OnClick({R.id.btn_create_class, R.id.btn_create_class1, R.id.btn_join_class, R.id.btn_join_class1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_class:
            case R.id.btn_create_class1:
                if (!UserInfoHelper.isGotoLogin(getActivity()))
                    startActivity(new Intent(getActivity(), GroupCreateActivity.class));
                break;
            case R.id.btn_join_class:
            case R.id.btn_join_class1:
                if (!UserInfoHelper.isGotoLogin(getActivity()))
                    startActivity(new Intent(getActivity(), GroupJoinActivity.class));
                break;
        }

    }

    @Subscribe(
            thread = EventThread.IO,
            tags = {
                    @Tag(BusAction.GROUPLIST)
            }
    )
    public void getList(String group) {
        mPresenter.loadData(true);
    }


    @Override
    public void showMyGroupList(List<ClassInfo> classInfos) {
        if (classInfos != null && classInfos.size() > 0) {
            llDataContainer.setVisibility(View.VISIBLE);
            llEmptyContainer.setVisibility(View.GONE);
            adapter.setData(classInfos);
        } else {
            llDataContainer.setVisibility(View.GONE);
            llEmptyContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMemberList(List<StudentInfo> count) {
        if (count.size() > 0) {
            mToolbar.setMenuIcon(R.mipmap.group65);
        } else {
            mToolbar.setMenuIcon(R.mipmap.group66);
        }
        getActivity().invalidateOptionsMenu();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String result) {
        mPresenter.loadData(true);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.UNREAD_MESSAGE)
            }
    )
    public void getMessage(Message message) {
        LogUtils.e(TAG, message);
        if (message.getContent() instanceof RichContentMessage) {
            adapter.setMessage(message.getTargetId(), message.getReceivedStatus().isRead());
        }

        adapter.notifyDataSetChanged();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.UNREAD_MESSAGE)
            }
    )
    public void getMessage(String message) {
        LogUtils.e(TAG, message);
        adapter.setMessage(message, true);
        adapter.notifyDataSetChanged();
    }

}
