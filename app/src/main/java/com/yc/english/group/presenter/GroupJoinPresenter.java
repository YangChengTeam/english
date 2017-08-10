package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.base.dao.ClassInfoDao;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupJoinContract;

import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.engin.GroupJoinEngin;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.GroupUser;
import com.yc.english.group.rong.models.GroupUserQueryResult;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/2 10:44.
 */

public class GroupJoinPresenter extends BasePresenter<GroupJoinEngin, GroupJoinContract.View> implements GroupJoinContract.Presenter {

    ClassInfoDao classInfoDao;
    public GroupJoinPresenter(Context context, GroupJoinContract.View view) {
        super(view);
        mEngin = new GroupJoinEngin();
        classInfoDao = GroupApp.getmDaoSession().getClassInfoDao();
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    public void joinGroup(final String groupId, final String groupName) {
//        final String[] userIds = new String[]{GoagalInfo.get().uuid};
//
//        ContactNotificationMessage contactNotificationMessage = ContactNotificationMessage.obtain(ContactNotificationMessage.CONTACT_OPERATION_REQUEST, "", groupId, "加一下好友");
//        Message message = Message.obtain(groupId, null, contactNotificationMessage);
//
//        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
//
//
//            @Override
//            public void onAttached(Message message) {
//
//            }
//
//            @Override
//            public void onSuccess(Message message) {
//                LogUtils.e(message.getContent());
//            }
//
//            @Override
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        final String[] userIds = new String[]{userInfo.getUid()};
        ImUtils.queryGroup(groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<GroupUserQueryResult>() {
            @Override
            public void call(GroupUserQueryResult groupUserQueryResult) {
                if (groupUserQueryResult.getCode() == 200) {
                    final List<GroupUser> users = groupUserQueryResult.getUsers();
                    ImUtils.joinGroup(userIds, groupId, groupName).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CodeSuccessResult>() {
                        @Override
                        public void call(CodeSuccessResult codeSuccessResult) {
                            if (codeSuccessResult.getCode() == 200) {//加入成功
                                ToastUtils.showShort("加入成功");
                                mView.startGroupChat(groupId, groupName);
                                ClassInfo info = new ClassInfo("", groupName, users.size()+"", Integer.parseInt(groupId));
                                classInfoDao.insert(info);
                                RxBus.get().post(BusAction.GROUPLIST, "from groupjoin");
                            }
                        }
                    });
                } else {
                    ToastUtils.showShort("没有该群组，请重新输入");
                }
            }
        });

    }
}
