package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;

import com.blankj.utilcode.util.EmptyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupApplyJoinContract;

import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupApplyJoinPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.activitys.teacher.GroupMemberActivity;
import com.yc.english.main.hepler.UserInfoHelper;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FullScreenActivity<GroupApplyJoinPresenter> implements BaseToolBar.OnItemClickLisener, GroupApplyJoinContract.View {

    private static final String TAG = "ChatActivity";


    private GroupInfo group;

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //rong://com.yc.english/conversation/group?targetId=654321&title=%E9%BE%99
            String groupId = null;
            String title = null;
            Uri data = intent.getData();
            if (data.getQueryParameter("title") != null) {
                title = data.getQueryParameter("title");
                mToolbar.setTitle(title);
            }
            if (data.getQueryParameter("targetId") != null) {
                groupId = data.getQueryParameter("targetId");
            }

            group = new GroupInfo(groupId, title);

        }


    }

    @Override
    public void init() {

        mPresenter = new GroupApplyJoinPresenter(this, this);
        initData();
        if (EmptyUtils.isNotEmpty(group)) {
            mPresenter.queryGroupById(this, group.getId(), "");
            RxBus.get().post(BusAction.UNREAD_MESSAGE, group.getId());
        }
        mToolbar.showNavigationIcon();

    }

    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        return R.layout.group_activity_chat;
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(this, GroupMemberActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }

    @Override
    public void showGroup(ClassInfo classInfo) {
        if (classInfo != null && classInfo.getMaster_id() != null) {
            if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                mToolbar.setMenuIcon(R.mipmap.group9);
                mToolbar.setOnItemClickLisener(this);
                mToolbar.setTitle(classInfo.getClassName());
                invalidateOptionsMenu();
            }
        }
    }

    @Override
    public void apply(int type) {

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
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String result) {
        mPresenter.queryGroupById(this, group.getId(), "");
    }
}
