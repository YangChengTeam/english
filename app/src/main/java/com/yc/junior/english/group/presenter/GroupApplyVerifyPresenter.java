package com.yc.junior.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.contract.GroupApplyVerifyContract;
import com.yc.junior.english.group.model.bean.ClassInfo;
import com.yc.junior.english.group.model.bean.ClassInfoWarpper;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.model.bean.StudentInfoWrapper;
import com.yc.junior.english.group.model.engin.GroupApplyVerifyEngine;
import com.yc.junior.english.group.rong.models.CodeSuccessResult;
import com.yc.junior.english.group.rong.util.RongIMUtil;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.hepler.UserInfoHelper;

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


    }

    /**
     * 获取申请加群列表
     *
     * @param class_id
     * @param status
     */
    public void getMemberList(String class_id, final int page, int page_size, String status, String master_id, String type, final boolean isFirst) {
        if (page == 1 && isFirst) {
            mView.showLoading();
        }
        Subscription subscription = EngineUtils.getMemberList(mContext, class_id, page, page_size, status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1 && isFirst) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> stringResultInfo) {
                ResultInfoHelper.handleResultInfo(stringResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (stringResultInfo.data != null && stringResultInfo.data.getList() != null && stringResultInfo.data.getList().size() > 0) {
                            mView.showVerifyList(stringResultInfo.data.getList());
                            mView.hideStateView();
                        } else {
                            if (page == 1) {
                                mView.showNoData();
                            }
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
    public void acceptApply(final StudentInfo studentInfo, final int position) {
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
                        mView.showApplyResult(stringResultInfo.data, position);
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
        Subscription subscription = EngineUtils.queryGroupById(mContext, studentInfo.getClass_id(), "").subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
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
                            ClassInfo classInfo = classInfoWarpperResultInfo.data.getInfo();
                            if (classInfo.getIs_allow_talk() == 0) {
                                addForbidMember(studentInfo);
                            }
                            String className = "群";
                            if ("1".equals(classInfo.getType())) {
                                className = "公会";
                            }
                            RongIMUtil.insertMessage("欢迎" + studentInfo.getNick_name() + "加入本" + className, studentInfo.getClass_id());
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
