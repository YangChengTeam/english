package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupSyncGroupContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.engin.GroupSyncGroupEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/7 11:16.
 */

public class GroupSyncGroupPresenter extends BasePresenter<GroupSyncGroupEngine, GroupSyncGroupContract.View> implements GroupSyncGroupContract.Presenter {
    public GroupSyncGroupPresenter(Context context, GroupSyncGroupContract.View view) {
        super(context, view);
        mEngin = new GroupSyncGroupEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        String uid = UserInfoHelper.getUserInfo().getUid();
        getGroupList(mContext, uid, "1");
    }

    public void getGroupList(Context context, String user_id, String is_admin) {
        Subscription subscription = EngineUtils.getMyGroupList(context, user_id, is_admin).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override

            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfoListResultInfo) {
                handleResultInfo(classInfoListResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showMyGroupList(classInfoListResultInfo.data.getList());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
