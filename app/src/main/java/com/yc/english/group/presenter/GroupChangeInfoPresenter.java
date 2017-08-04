package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupChangeInfoContract;
import com.yc.english.group.model.engin.GroupChangeInfoEngine;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/4 14:17.
 * 修改个人信息
 */

public class GroupChangeInfoPresenter extends BasePresenter<GroupChangeInfoEngine, GroupChangeInfoContract.View> implements GroupChangeInfoContract.Presenter {
    public GroupChangeInfoPresenter(Context context, GroupChangeInfoContract.View view) {
        super(context, view);
        mEngin = new GroupChangeInfoEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    @Override
    public void changeGroupInfo(String class_id, String name, String face, String vali_type) {
        Subscription subscription = mEngin.changeGroupInfo(class_id, name, face, vali_type).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
//                        ImUtils.createGroup()
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
