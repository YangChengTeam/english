package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupSyncGroupContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.group.view.activitys.teacher.GroupSyncGroupListActivity;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

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
