package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupForbidMemberContract;
import com.yc.english.group.model.engin.GroupForbidMemberEngine;
import com.yc.english.group.rong.models.CodeSuccessResult;

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

    }

    @Override
    public void addForbidMember(String userId, String groupId, String minute) {
        mView.showLoadingDialog("添加禁言成员中，请稍候...");
        Subscription subscription = mEngin.addForbidMember(userId, groupId, minute).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
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
                    mView.showForbidResult();
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void rollBackMember(String[] userId, String groupId) {
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
                    mView.showRollBackResult();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
