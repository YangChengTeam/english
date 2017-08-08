package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupUpdatePublishTaskContract;
import com.yc.english.group.model.engin.GroupUpdatePublishTaskEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:17.
 * 老师修改作业
 */

public class GroupUpdatePublishTaskPresenter extends BasePresenter<GroupUpdatePublishTaskEngine, GroupUpdatePublishTaskContract.View> implements GroupUpdatePublishTaskContract.Presenter {
    public GroupUpdatePublishTaskPresenter(Context context, GroupUpdatePublishTaskContract.View view) {
        super(context, view);
        mEngin = new GroupUpdatePublishTaskEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void updatePublishTask(String id, String publisher, String desp, String imgs, String voices, String docs) {
        Subscription subscription = mEngin.updatePublishTask(id, publisher, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<String>>() {
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
