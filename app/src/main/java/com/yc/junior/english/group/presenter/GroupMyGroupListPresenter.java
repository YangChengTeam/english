package com.yc.junior.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.contract.GroupMyGroupListContract;
import com.yc.junior.english.group.model.bean.ClassInfoList;
import com.yc.junior.english.group.model.bean.StudentInfoWrapper;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.UserInfo;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/3 11:58.
 */

public class GroupMyGroupListPresenter extends BasePresenter<BaseEngin, GroupMyGroupListContract.View> implements GroupMyGroupListContract.Presenter {
    public GroupMyGroupListPresenter(Context context, GroupMyGroupListContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            getMyGroupList(mContext, uid, "-1", "0");
            getMemberList(mContext, "", "0", uid, "0");
        } else {
            mView.showMyGroupList(null);
        }
    }

    private boolean isEmpty = true;

    @Override
    public void getMyGroupList(Context context, String user_id, String role, String type) {
        if (isEmpty) {
            mView.showLoading();
        }
        Subscription subscription = EngineUtils.getMyGroupList(context, user_id, role, type).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
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
                        if (classInfoResultInfo.data != null && classInfoResultInfo.data.getList() != null && classInfoResultInfo.data.getList().size() > 0) {
                            isEmpty = false;
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id, String type) {
        Subscription subscription = EngineUtils.getMemberList(context, class_id, 1, 10, status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
                        mView.showMemberList(studentInfoWrapperResultInfo.data.getList());

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
