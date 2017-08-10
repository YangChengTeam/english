package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupUpdateDoTaskContract;
import com.yc.english.group.model.engin.GroupUpdateDoTaskEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 17:39.
 */

public class GroupUpdateDoTaskPresenter extends BasePresenter<GroupUpdateDoTaskEngine, GroupUpdateDoTaskContract.View> implements GroupUpdateDoTaskContract.Presenter {
    public GroupUpdateDoTaskPresenter(Context context, GroupUpdateDoTaskContract.View view) {
        super(context, view);
        mEngin = new GroupUpdateDoTaskEngine(context);
    }

    @Override
    public void updateDoTask(String id, String user_id, String desp, String imgs, String voices, String docs) {
        Subscription subscription = mEngin.updateDoTask(id, user_id, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<String>>() {
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
