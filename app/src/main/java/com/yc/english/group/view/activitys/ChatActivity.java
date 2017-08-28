package com.yc.english.group.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
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
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.presenter.GroupApplyJoinPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.activitys.teacher.GroupMemberActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.setting.view.activitys.PersonCenterActivity;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FullScreenActivity<GroupApplyJoinPresenter> implements BaseToolBar.OnItemClickLisener, GroupApplyJoinContract.View {

    private static final String TAG = "ChatActivity";

    private String groupId = null;
    private String title = null;

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //rong://com.yc.english/conversation/group?targetId=654321&title=%E9%BE%99

            Uri data = intent.getData();
            if (data.getQueryParameter("title") != null) {
                title = data.getQueryParameter("title");
                mToolbar.setTitle(title);
            }
            if (data.getQueryParameter("targetId") != null) {
                groupId = data.getQueryParameter("targetId");
            }
            mToolbar.setMenuIcon(R.mipmap.group9);
            mToolbar.showNavigationIcon();
            mToolbar.setOnItemClickLisener(this);
            mToolbar.setTitle(title);
            GroupInfo group = new GroupInfo(groupId, title);
            GroupInfoHelper.setGroupInfo(group);
            mPresenter.queryGroupById(this, groupId, "");
        }

    }

    @Override
    public void init() {
        mPresenter = new GroupApplyJoinPresenter(this, this);
        initData();
        initListener();
    }

    private void initListener() {

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                if (userInfo.getUserId().equals(UserInfoHelper.getUserInfo().getUid())) {
                    startActivity(new Intent(ChatActivity.this, PersonCenterActivity.class));
                }
                return false;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            @Override
            public boolean onMessageClick(Context context, View view, Message message) {
                return false;
            }

            @Override
            public boolean onMessageLinkClick(Context context, String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });

    }

    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        return R.layout.group_activity_chat;
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(this, GroupMemberActivity.class);
        startActivity(intent);
    }

    @Override
    public void showGroup(ClassInfo classInfo) {
        GroupInfoHelper.setClassInfo(classInfo);
        mPresenter.getMemberList(GroupInfoHelper.getGroupInfo().getId(), "1", "", GroupInfoHelper.getClassInfo().getFlag());
    }

    @Override
    public void apply(int type) {
    }

    @Override
    public void showMemberList(final List<UserInfo> list) {
        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback callback) {
                //获取群组成员信息列表
                callback.onGetGroupMembersResult(list); // 调用 callback 的 onGetGroupMembersResult 回传群组信息
            }
        });
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
        mToolbar.setTitle(result);
    }

}
