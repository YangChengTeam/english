package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskContract;
import com.yc.english.group.model.engin.GroupDoTaskEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 17:30.
 */

public class GroupDoTaskPresenter extends BasePresenter<GroupDoTaskEngine, GroupDoTaskContract.View> implements GroupDoTaskContract.Presenter {
    public GroupDoTaskPresenter(Context context, GroupDoTaskContract.View view) {
        super(context, view);
        mEngin = new GroupDoTaskEngine(context);
    }

    @Override
    public void doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs) {
        Subscription subscription = mEngin.doTask(class_id, user_id, task_id, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<String>>() {
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
