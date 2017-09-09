package com.yc.english.union.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.R;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.GroupApplyInfo;
import com.yc.english.group.model.bean.MemberInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionListContract;

import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/9/7 12:15.
 */

public class UnionListPresenter extends BasePresenter<BaseEngin, UnionListContract.View> implements UnionListContract.Presenter {
    public UnionListPresenter(Context context, UnionListContract.View view) {
        super(context, view);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    public void getUnionList(String type, String flag, final int page, int page_size, final boolean isFitst) {
        if (page == 1 && isFitst) {
            mView.showLoading();
        }
        Subscription subscription = EngineUtils.getUnionList(mContext, type, flag, page, page_size).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1&& isFitst) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1&& isFitst) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1&& isFitst) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (page == 1&& isFitst) {
                            mView.hideStateView();
                        }
                        mView.showUnionList(classInfo.data.getList(), page, isFitst);
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }


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
                        mView.showMemberList(studentInfoWrapperResultInfo.data.getList());

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


    public void isUnionMember(final ClassInfo classInfo) {
        Subscription subscription = EngineUtils.isGroupMember(mContext, classInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid()).subscribe(new Subscriber<ResultInfo<MemberInfo>>() {
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
                        mView.showIsMember(stringResultInfo.data.getIs_member(), classInfo);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 申请加入班群
     *
     * @param user_id
     * @param classInfo
     */

    public void applyJoinGroup(String user_id, final ClassInfo classInfo) {

        mView.showLoadingDialog("正在申请加入班级，请稍候");
        Subscription subscription = EngineUtils.applyJoinGroup(mContext, user_id, classInfo.getGroupId() + "").subscribe(new Subscriber<ResultInfo<GroupApplyInfo>>() {
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
                        if (stringResultInfo != null && stringResultInfo.data != null) {
                            GroupApplyInfo applyInfo = stringResultInfo.data;
                            int type = Integer.parseInt(applyInfo.getVali_type());

                            if (type == GroupConstant.CONDITION_ALL_ALLOW) {
                                ToastUtils.showShort(mContext.getString(R.string.congratulation_join_group));
                                RxBus.get().post(BusAction.GROUP_LIST, "from groupjoin");

                                StudentInfo studentInfo = new StudentInfo();
                                studentInfo.setUser_id(applyInfo.getUser_id());
                                studentInfo.setClass_id(applyInfo.getClass_id());
                                if (classInfo.getIs_allow_talk()==0) {
                                    addForbidMember(studentInfo);
                                }
                                setMode(classInfo);
                            } else if (type == GroupConstant.CONDITION_VERIFY_JOIN) {

                                ToastUtils.showShort(mContext.getString(R.string.commit_apply_join));
                            }

                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


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

    private void setMode(ClassInfo classInfo) {

        GroupApp.setMyExtensionModule(false, false);
        RongIM.getInstance().startGroupChat(mContext, classInfo.getClass_id(), classInfo.getClassName());
    }
}
