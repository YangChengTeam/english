package com.yc.junior.english.group.view.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseToolBar;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.contract.GroupApplyJoinContract;
import com.yc.junior.english.group.model.bean.ClassInfo;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.presenter.GroupApplyJoinPresenter;
import com.yc.junior.english.group.rong.models.GroupInfo;
import com.yc.junior.english.group.view.activitys.teacher.GroupMemberActivity;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.setting.view.activitys.PersonCenterActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FullScreenActivity<GroupApplyJoinPresenter> implements GroupApplyJoinContract.View, BaseToolBar.OnItemClickLisener {

    private static final String TAG = "ChatActivity";

    private String groupId = null;
    private String title = null;


    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //rong://com.yc.junior.english/conversation/group?targetId=654321&title=%E9%BE%99

            Uri data = intent.getData();
            if (data.getQueryParameter("title") != null) {

                title = data.getQueryParameter("title");

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

    private void setTint() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("免责声明");
        builder.setMessage(getString(R.string.relief_introduce));
        builder.setPositiveButton(getString(R.string.i_konw), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    public void init() {

        View view = mToolbar.findViewById(R.id.container);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height=SizeUtils.dp2px(50);
        view.setLayoutParams(layoutParams);

        mPresenter = new GroupApplyJoinPresenter(this, this);
        initData();
        initListener();


    }

    private void initListener() {

        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                if (userInfo.getUserId().equals(UserInfoHelper.getUserInfo().getUid())) {
                    startActivity(new Intent(ChatActivity.this, PersonCenterActivity.class), ActivityOptionsCompat.makeSceneTransitionAnimation(ChatActivity.this).toBundle());
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        hasChangeStatus = false;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        return R.layout.group_activity_chat;
    }


    @Override
    public void showGroup(ClassInfo classInfo) {
        GroupInfoHelper.setClassInfo(classInfo);
        mPresenter.getMemberList(GroupInfoHelper.getGroupInfo().getId(), "1", 1, 1000, "", GroupInfoHelper.getClassInfo().getType());
        if (GroupInfoHelper.getClassInfo().getType().equals("1") && !SPUtils.getInstance().getBoolean(GroupInfoHelper.getClassInfo().getClass_id() + "isFirst", false)) {
            setTint();
            SPUtils.getInstance().put(GroupInfoHelper.getClassInfo().getClass_id() + "isFirst", true);
        }
    }

    private List<StudentInfo> studentInfoList;

    @Override
    public void showMemberList(final List<UserInfo> list, final List<StudentInfo> dataList) {
        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(String groupId, RongIM.IGroupMemberCallback callback) {
                //获取群组成员信息列表
                callback.onGetGroupMembersResult(list); // 调用 callback 的 onGetGroupMembersResult 回传群组信息
            }
        });
        studentInfoList = dataList;
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

    @Override
    public void onClick() {
        Intent intent = new Intent(ChatActivity.this, GroupMemberActivity.class);
        intent.putParcelableArrayListExtra("studentList", (ArrayList) studentInfoList);
        startActivity(intent);
    }


}
