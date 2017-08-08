package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskListContract;
import com.yc.english.group.model.engin.GroupDoTaskListEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:01.
 */

public class GroupDoTaskListPresenter extends BasePresenter<GroupDoTaskListEngine, GroupDoTaskListContract.View> implements GroupDoTaskListContract.Presenter {
    public GroupDoTaskListPresenter(Context context, GroupDoTaskListContract.View view) {
        super(context, view);
        mEngin = new GroupDoTaskListEngine(context);
    }

    @Override
    public void getDoTaskList(String class_id, String user_id) {
        Subscription subscription = mEngin.getDoTaskList(class_id, user_id).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
