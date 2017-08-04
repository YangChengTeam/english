package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDeleteMemberContract;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.bean.StudentRemoveInfo;
import com.yc.english.group.model.engin.GroupDeleteMemberEngine;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.utils.EngineUtils;

import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/2 19:25.
 */

public class GroupDeleteMemberPresenter extends BasePresenter<GroupDeleteMemberEngine, GroupDeleteMemberContract.View> implements GroupDeleteMemberContract.Presenter {
    public GroupDeleteMemberPresenter(Context context, GroupDeleteMemberContract.View view) {
        super(view);
        mEngin = new GroupDeleteMemberEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    @Override
    public void deleteMember(String class_id, String master_id, String[] members) {
        Subscription subscription = mEngin.deleteMember(class_id, master_id, members).subscribe(new Subscriber<ResultInfo<StudentRemoveInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentRemoveInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        StudentRemoveInfo removeInfo = stringResultInfo.data;
                        List<String> members = removeInfo.getMembers();
                        String[] userIds = members.toArray(new String[members.size()]);
                        removeRongGroup(userIds, removeInfo.getClass_id());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id) {
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showMemberList(stringResultInfo.data.getList());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 将成员移除群组
     *
     * @param userId
     * @param groupId
     */

    public void removeRongGroup(final String[] userId, final String groupId) {
        ImUtils.quitGroup(userId, groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CodeSuccessResult>() {
            @Override
            public void call(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult.getCode() == 200) {//删除成功
                    mView.showDeleteResult();
                }
            }
        });
    }

}
