package com.yc.english.group.view.activitys.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupForbidMemberContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupForbidMemberPresenter;
import com.yc.english.group.rong.models.GagGroupUser;
import com.yc.english.group.view.adapter.GroupForbidTalkAdapter;
import com.yc.english.news.view.widget.NewsScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/29 09:13.
 */

public class GroupForbidTalkActivity extends FullScreenActivity<GroupForbidMemberPresenter> implements CompoundButton.OnCheckedChangeListener, GroupForbidMemberContract.View {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_ll_add_forbid_member)
    LinearLayout mLlAddForbidMember;
    @BindView(R.id.mSwitchCompat)
    SwitchCompat mSwitchCompat;
    @BindView(R.id.m_tv_forbid_hint)
    TextView mTvForbidHint;

    private GroupForbidTalkAdapter adapter;


    @Override
    public void init() {
        mPresenter = new GroupForbidMemberPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_forbid));
        mToolbar.showNavigationIcon();

        mSwitchCompat.setChecked(GroupInfoHelper.getClassInfo().getIs_allow_talk() == 0);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupForbidTalkAdapter(null);
        mRecyclerView.setAdapter(adapter);
        BaseItemDecoration itemDecoration = new BaseItemDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);
        if (GroupInfoHelper.getClassInfo().getType().equals("1")) {
            mTvForbidHint.setText("开启后，只允许会主发言");
        }
        initListener();

    }

    private void initListener() {
        mSwitchCompat.setOnCheckedChangeListener(this);
        RxView.clicks(mLlAddForbidMember).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivityForResult(new Intent(GroupForbidTalkActivity.this, GroupForbidMemberActivity.class), 200);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mStudentInfo = (StudentInfo) adapter.getItem(position);
                mPresenter.rollBackMember(new String[]{mStudentInfo.getUser_id()}, mStudentInfo.getNick_name(), mStudentInfo.getClass_id(), position, false);
                return false;

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_forbid_talk;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (allList != null && allList.size() > 0) {
                mPresenter.changeGroupInfo(GroupInfoHelper.getGroupInfo().getId(), "0");
                for (StudentInfo studentInfo : allList) {
                    mPresenter.addForbidMember(studentInfo, "0", true);
                }
            } else {
                mSwitchCompat.setChecked(false);
            }
        } else {
            String[] strs = new String[allList.size()];
            for (int i = 0; i < allList.size(); i++) {
                strs[i] = allList.get(i).getUser_id();
            }
            mPresenter.changeGroupInfo(GroupInfoHelper.getGroupInfo().getId(), "1");
            mPresenter.rollBackMember(strs, null, GroupInfoHelper.getGroupInfo().getId(), -1, true);
        }

    }

    private List<StudentInfo> studentInfoList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            List<StudentInfo> studentList = data.getParcelableArrayListExtra("studentList");

            showDialog(studentList);
        }
    }


    public void showDialog(final List<StudentInfo> studentList) {

        // 创建数据
        final String[] items = getResources().getStringArray(R.array.forbid_time);
        // 创建对话框构建器

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle(getString(R.string.add_forbid_member))
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for (StudentInfo studentInfo : studentList) {

                            studentInfo.setForbidTime(items[which]);
                            studentInfo.setIsForbid(true);
                            mPresenter.addForbidMember(studentInfo, getTime(which), false);
                            studentInfoList.add(studentInfo);

                        }

                    }
                });
        builder.create().show();
    }

    private int count = 1;

    @Override
    public void showForbidResult(StudentInfo studentInfo, boolean allForbid) {
        if (!allForbid) {

            adapter.setNewData(studentInfoList);

            insertMessage(studentInfo.getNick_name(), studentInfo.getClass_id(), studentInfo.getForbidTime(), true);
            saveForbidStu(studentInfoList);
        } else {
            if (count >= 1) {
                insertMessage(null, GroupInfoHelper.getGroupInfo().getId(), null, true);
                count--;
            }

        }


    }

    private void saveForbidStu(List<StudentInfo> studentInfoList) {

        SPUtils.getInstance().put(GroupConstant.FORBID_MEMBER + GroupInfoHelper.getGroupInfo().getId(), JSONObject.toJSONString(studentInfoList));

    }


    public String getTime(int which) {
        String strTime = null;
        switch (which) {
            case 0:
                strTime = "5";
                break;
            case 1:
                strTime = "60";
                break;
            case 2:
                strTime = 24 * 60 + "";
                break;
            case 3:
                strTime = 7 * 24 * 60 + "";
                break;
            case 4:
                strTime = 30 * 24 * 60 + "";
                break;
        }
        return strTime;
    }

    private StudentInfo mStudentInfo;


    @Override
    public void showRollBackResult(String[] userId, String nickName, String groupId, int position, boolean allForbid) {
        if (allForbid) {
            insertMessage(null, GroupInfoHelper.getGroupInfo().getId(), null, false);
            studentInfoList.clear();
            adapter.notifyDataSetChanged();
            SPUtils.getInstance().put(GroupConstant.FORBID_MEMBER + GroupInfoHelper.getGroupInfo().getId(), "");
        } else {
            studentInfoList.remove(mStudentInfo);
            insertMessage(nickName, groupId, null, false);
            clearForbidStu(userId[0]);
            adapter.notifyItemRemoved(position);
        }

    }

    private List<StudentInfo> allList;

    @Override
    public void showLisGagUserResult(List<GagGroupUser> users, List<StudentInfo> list) {
        list.remove(0);
        allList = list;
        getForbidStu(users);
    }


    private void getForbidStu(List<GagGroupUser> users) {
        if (users != null && users.size() > 0) {

            String str = SPUtils.getInstance().getString(GroupConstant.FORBID_MEMBER + GroupInfoHelper.getGroupInfo().getId());

            List<StudentInfo> list = JSONObject.parseArray(str, StudentInfo.class);
            for (StudentInfo studentInfo : list) {
                for (GagGroupUser user : users) {
                    if (user.getUserId().equals(studentInfo.getUser_id())) {
                        studentInfoList.add(studentInfo);
                        break;
                    }
                }
            }
            adapter.setNewData(studentInfoList);
            saveForbidStu(studentInfoList);
        } else {
            SPUtils.getInstance().put(GroupConstant.FORBID_MEMBER + GroupInfoHelper.getGroupInfo().getId(), "");
        }

    }

    private StudentInfo curStu;

    private void clearForbidStu(String userId) {
        String str = SPUtils.getInstance().getString(GroupConstant.FORBID_MEMBER + GroupInfoHelper.getGroupInfo().getId());
        boolean unForbid = false;
        List<StudentInfo> studentInfos = JSONObject.parseArray(str, StudentInfo.class);
        if (studentInfos != null && studentInfos.size() > 0) {
            for (StudentInfo info : studentInfos) {
                if (userId.equals(info.getUser_id())) {
                    unForbid = true;
                    curStu = info;
                    break;
                }
            }
            if (unForbid) {
                studentInfos.remove(curStu);
                saveForbidStu(studentInfos);
            }
        }
    }

    public void insertMessage(String nickName, String groupId, String time, boolean forbid) {
        InformationNotificationMessage message;
        if (forbid) {
            if (TextUtils.isEmpty(nickName)) {
                message = InformationNotificationMessage.obtain("该群开启了全员禁言");
            } else {
                message = InformationNotificationMessage.obtain(nickName + "已被群主禁言" + time);
            }
        } else {
            if (TextUtils.isEmpty(nickName)) {
                message = InformationNotificationMessage.obtain("该群关闭了全员禁言");
            } else {
                message = InformationNotificationMessage.obtain(nickName + "已被群主解禁");
            }
        }
        Message message1 = Message.obtain(groupId, Conversation.ConversationType.GROUP, message);
        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });

    }

}
