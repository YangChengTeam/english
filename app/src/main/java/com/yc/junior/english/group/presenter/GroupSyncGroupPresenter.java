package com.yc.junior.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.contract.GroupSyncGroupContract;
import com.yc.junior.english.group.model.bean.ClassInfoList;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/7 11:16.
 */

public class GroupSyncGroupPresenter extends BasePresenter<BaseEngin, GroupSyncGroupContract.View> implements GroupSyncGroupContract.Presenter {
    public GroupSyncGroupPresenter(Context context, GroupSyncGroupContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        String uid = UserInfoHelper.getUserInfo().getUid();
        getGroupList(mContext, uid, "2", GroupInfoHelper.getClassInfo().getType());

    }

    public void getGroupList(Context context, String user_id, String role, String type) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMyGroupList(context, user_id, role, type).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override

            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfoListResultInfo) {
                ResultInfoHelper.handleResultInfo(classInfoListResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void reulstInfoOk() {

                        if (classInfoListResultInfo != null && classInfoListResultInfo.data != null
                                && classInfoListResultInfo.data.getList() != null && classInfoListResultInfo.data.getList().size() > 1) {

                            mView.showMyGroupList(classInfoListResultInfo.data.getList());
                            mView.hideStateView();
                        } else {
                            mView.showNoData();
                        }

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
