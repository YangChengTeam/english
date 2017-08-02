package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.contract.GroupCreateContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.engin.GroupCreateEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/7/24 18:38.
 */

public class GroupCreatePresenter extends BasePresenter<GroupCreateEngine, GroupCreateContract.View> implements GroupCreateContract.Presenter {


    public GroupCreatePresenter(Context context, GroupCreateContract.View view) {
        super(view);
        mEngin = new GroupCreateEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

    }


    @Override
    public void createGroup(String user_id, String groupName, String face) {
        Subscription subscription = mEngin.createGroup(user_id, groupName, face).subscribe(new Subscriber<ResultInfo<ClassInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<ClassInfo> classInfoResultInfo) {
                if (classInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    mView.showCreateResult(classInfoResultInfo.data);
                }


            }
        });
        mSubscriptions.add(subscription);
    }
}
