package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.engin.GroupPublishTaskDetailEngine;

import io.rong.imageloader.cache.disc.naming.FileNameGenerator;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:31.
 */

public class GroupPublishTaskDetailPresenter extends BasePresenter<GroupPublishTaskDetailEngine, GroupPublishTaskDetailContract.View> implements GroupPublishTaskDetailContract.Presenter {
    public GroupPublishTaskDetailPresenter(Context context, GroupPublishTaskDetailContract.View view) {
        super(context, view);
        mEngin = new GroupPublishTaskDetailEngine(context);
    }

    @Override
    public void getPublishTaskDetail(String id, String user_id) {
        Subscription subscription = mEngin.getPublishTaskDetail(id, user_id).subscribe(new Subscriber<ResultInfo<String>>() {
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
