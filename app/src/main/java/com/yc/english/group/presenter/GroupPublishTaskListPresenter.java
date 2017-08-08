package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupPublishTaskListContract;
import com.yc.english.group.model.engin.GroupPublishTaskListEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 15:52.
 */

public class GroupPublishTaskListPresenter extends BasePresenter<GroupPublishTaskListEngine, GroupPublishTaskListContract.View> implements GroupPublishTaskListContract.Presenter {
    public GroupPublishTaskListPresenter(Context context, GroupPublishTaskListContract.View view) {
        super(context, view);
        mEngin = new GroupPublishTaskListEngine(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getPublishTaskList(String publisher, String class_id) {
        Subscription subscription = mEngin.getPublishTaskList(publisher, class_id).subscribe(new Subscriber<ResultInfo<String>>() {
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
}
