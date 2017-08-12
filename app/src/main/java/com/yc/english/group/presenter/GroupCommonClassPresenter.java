package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.engin.GroupCommonClassEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/11 15:37.
 */

public class GroupCommonClassPresenter extends BasePresenter<GroupCommonClassEngine, GroupCommonClassContract.View> implements GroupCommonClassContract.Presenter {
    public GroupCommonClassPresenter(Context context, GroupCommonClassContract.View view) {
        super(context, view);
        mEngin = new GroupCommonClassEngine(context);
    }

    @Override
    public void getCommonClassList() {
        Subscription subscription = mEngin.getCommonClassList().subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<ClassInfoList> classInfoListResultInfo) {
                mView.showCommonClassList(classInfoListResultInfo.data.getList());
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getCommonClassList();
    }
}
