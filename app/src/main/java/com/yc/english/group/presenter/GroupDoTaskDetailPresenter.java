package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskDetailContract;
import com.yc.english.group.model.engin.GroupDoTaskDetailEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:09.
 */

public class GroupDoTaskDetailPresenter extends BasePresenter<GroupDoTaskDetailEngine, GroupDoTaskDetailContract.View> implements GroupDoTaskDetailContract.Presenter {
    public GroupDoTaskDetailPresenter(Context context, GroupDoTaskDetailContract.View view) {
        super(view);
        mEngin = new GroupDoTaskDetailEngine(context);
    }

    @Override
    public void getDoTaskDetail(String id, String user_id) {
        Subscription subscription = mEngin.getDoTaskDetail(id, user_id).subscribe(new Subscriber<ResultInfo<String>>() {
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
