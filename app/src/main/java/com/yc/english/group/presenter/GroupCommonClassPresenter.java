package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.GroupApplyInfo;
import com.yc.english.group.model.bean.MemberInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupCommonClassEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/11 15:37.
 */

public class GroupCommonClassPresenter extends BasePresenter<GroupCommonClassEngine, GroupCommonClassContract.View> implements GroupCommonClassContract.Presenter {
    public GroupCommonClassPresenter(Context context, GroupCommonClassContract.View view) {
        super(context, view);
        mEngin = new GroupCommonClassEngine(context);
    }

    @Override
    public void getCommonClassList() {
        mView.showLoading();
        Subscription subscription = mEngin.getCommonClassList().subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
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
                        ClassInfoList data = classInfoListResultInfo.data;
                        if (data != null && data.getList() != null && data.getList().size() > 0) {
                            mView.showCommonClassList(classInfoListResultInfo.data.getList());
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

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getCommonClassList();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            getMemberList("", "0", uid, "comm");
        }
    }

    @Override
    public void applyJoinGroup(String user_id, String sn) {

        mView.showLoadingDialog("正在申请加入班级，请稍候");
        Subscription subscription = EngineUtils.applyJoinGroup(mContext, user_id, sn).subscribe(new Subscriber<ResultInfo<GroupApplyInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
                LogUtils.e("onError: " + e.getMessage());
            }

            @Override
            public void onNext(final ResultInfo<GroupApplyInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                TipsHelper.tips(mContext, "你已提交申请，请等待管理员审核");
                            }
                        });

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void isGroupMember(String class_id, String user_id) {
        Subscription subscription = mEngin.isGroupMember(class_id, user_id).subscribe(new Subscriber<ResultInfo<MemberInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<MemberInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showIsMember(stringResultInfo.data.getIs_member());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMemberList(String class_id, String status, String master_id, String flag) {
        Subscription subscription = EngineUtils.getMemberList(mContext, class_id, status, master_id, flag).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
                        mView.showVerifyResult(studentInfoWrapperResultInfo.data.getList());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }
}
