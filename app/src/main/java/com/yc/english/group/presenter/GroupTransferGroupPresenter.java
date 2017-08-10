package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupTransferGroupContract;
import com.yc.english.group.model.engin.GroupTransferGroupEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/4 19:33.
 */

public class GroupTransferGroupPresenter extends BasePresenter<GroupTransferGroupEngine, GroupTransferGroupContract.View> implements GroupTransferGroupContract.Presenter {
    public GroupTransferGroupPresenter(Context context, GroupTransferGroupContract.View view) {
        super(context, view);
        mEngin = new GroupTransferGroupEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    @Override
    public void transferGroup(String class_id, String master_id, String user_name) {
        mView.showLoadingDialog("正在转让班群，请稍候");
        Subscription subscription = mEngin.transferGroup(class_id, master_id, user_name).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showTransferResult();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
