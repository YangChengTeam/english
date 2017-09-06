package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupForbidMemberContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupForbidMemberEngine;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.ListGagGroupUserResult;
import com.yc.english.group.utils.EngineUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/30 09:53.
 */

public class GroupForbidMemberPresenter extends BasePresenter<GroupForbidMemberEngine, GroupForbidMemberContract.View> implements GroupForbidMemberContract.Presenter {


    public GroupForbidMemberPresenter(Context context, GroupForbidMemberContract.View view) {
        super(context, view);
        mEngin = new GroupForbidMemberEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

        if (!forceUpdate) return;
        getMemberList(GroupInfoHelper.getGroupInfo().getId(), "1", "", GroupInfoHelper.getClassInfo().getFlag());
    }

    @Override
    public void addForbidMember(final StudentInfo studentInfo, final String minute, final boolean allForbid) {
        mView.showLoadingDialog("添加禁言成员中，请稍候...");
        Subscription subscription = EngineUtils.addForbidMember(studentInfo.getUser_id(), studentInfo.getClass_id(), minute).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {
                    mView.showForbidResult(studentInfo, allForbid);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void rollBackMember(final String[] userId, final String nickName, final String groupId, final boolean allForbid) {
        mView.showLoadingDialog("正在解禁用户，请稍候...");
        Subscription subscription = mEngin.rollBackMember(userId, groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {
                    mView.showRollBackResult(userId,nickName, groupId, allForbid);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getMemberList(final String sn, String status, String master_id, String flag) {
        Subscription subscription = EngineUtils.getMemberList(mContext, sn, status, master_id, flag).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> studentInfoWrapperResultInfo) {
                handleResultInfo(studentInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (studentInfoWrapperResultInfo.data != null && studentInfoWrapperResultInfo.data.getList() != null) {
                            lisGagUser(sn, studentInfoWrapperResultInfo.data.getList());
                        }

                    }
                });
            }
        });
        mSubscriptions.add(subscription);

    }

    @Override
    public void lisGagUser(String groupId, final List<StudentInfo> list) {
        Subscription subscription = EngineUtils.lisGagUser(groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ListGagGroupUserResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ListGagGroupUserResult listGagGroupUserResult) {

                if (listGagGroupUserResult.getCode() == 200) {
                    mView.showLisGagUserResult(listGagGroupUserResult.getUsers(), list);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

}
