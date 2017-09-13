package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupApplyVerifyContract;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.GroupApplyInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupApplyVerifyEngine;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/2 18:06.
 */

public class GroupApplyVerifyPresenter extends BasePresenter<GroupApplyVerifyEngine, GroupApplyVerifyContract.View> implements GroupApplyVerifyContract.Presenter {
    public GroupApplyVerifyPresenter(Context context, GroupApplyVerifyContract.View view) {
        super(context, view);
        mEngin = new GroupApplyVerifyEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

    }

    /**
     * 获取申请加群列表
     *
     * @param class_id
     * @param status
     */
    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id, String type) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMemberList(context, class_id,  status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (stringResultInfo.data.getList() != null && stringResultInfo.data.getList().size() > 0) {
                            mView.showVerifyList(stringResultInfo.data.getList());
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

    /**
     * 接受加群
     *
     * @param studentInfo
     */
    @Override
    public void acceptApply(final StudentInfo studentInfo) {
        Subscription subscription = mEngin.acceptApply(studentInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid(), studentInfo.getUser_id()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showApplyResult(stringResultInfo.data);
                        queryGroup(studentInfo);
                        RxBus.get().post(BusAction.GROUP_LIST, "join Group");

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void addForbidMember(StudentInfo studentInfo) {
        Subscription subscription = EngineUtils.addForbidMember(studentInfo.getUser_id(), studentInfo.getClass_id(), "0").observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {

                }

            }
        });
        mSubscriptions.add(subscription);
    }

    private void queryGroup(final StudentInfo studentInfo) {
        Subscription subscription = EngineUtils.queryGroupById(mContext, "", studentInfo.getSn()).subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoWarpper> classInfoWarpperResultInfo) {
                handleResultInfo(classInfoWarpperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (classInfoWarpperResultInfo != null && classInfoWarpperResultInfo.data != null) {
                            if (classInfoWarpperResultInfo.data.getInfo().getIs_allow_talk() == 0) {
                                addForbidMember(studentInfo);
                            }
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
