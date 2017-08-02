package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.utils.UIUitls;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.engin.GroupListEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/7/31 17:54.
 */

public class GroupListPresenter extends BasePresenter<GroupListEngine, GroupListContract.View> implements GroupListContract.Presenter {
    public GroupListPresenter(Context context, GroupListContract.View view) {
        super(view);
        mEngin = new GroupListEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getGroupList();
    }

    @Override
    public void getGroupList() {
        Subscription subscribe = mEngin.getGroupList().subscribe(new Subscriber<List<ClassInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<ClassInfo> classInfos) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showGroupList(classInfos);
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }
}
