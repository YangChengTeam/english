package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupMyGroupListContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupMyGroupListEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/3 11:58.
 */

public class GroupMyGroupListPresenter extends BasePresenter<GroupMyGroupListEngine, GroupMyGroupListContract.View> implements GroupMyGroupListContract.Presenter {
    public GroupMyGroupListPresenter(Context context, GroupMyGroupListContract.View view) {
        super(context, view);
        mEngin = new GroupMyGroupListEngine(context);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            getMyGroupList(mContext, uid, "0");
            getMemberList(mContext, "", "0", uid);
        } else {
            mView.showMyGroupList(null);
        }
    }

    @Override
    public void getMyGroupList(Context context, String user_id, String is_admin) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMyGroupList(context, user_id, is_admin).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(classInfoResultInfo, new ResultInfoHelper.Callback() {
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
                        mView.hideStateView();
                        mView.showMyGroupList(classInfoResultInfo.data.getList());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id) {
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id, "").subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> studentInfoWrapperResultInfo) {
                handleResultInfo(studentInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                mView.showMemberList(studentInfoWrapperResultInfo.data.getList());
                            }
                        });


                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
