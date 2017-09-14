package com.yc.english.union.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
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
import com.yc.english.group.rong.util.RongIMUtil;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionCommonListContract;

import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/9/7 12:15.
 */

public class UnionCommonListPresenter extends BasePresenter<BaseEngin, UnionCommonListContract.View> implements UnionCommonListContract.Presenter {
    public UnionCommonListPresenter(Context context, UnionCommonListContract.View view) {
        super(context, view);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

    }

    private boolean isEmpty = true;

    public void getUnionList1(String user_id, String role, String type) {
        if (isEmpty) {
            mView.showLoading();
        }

        Subscription subscription = EngineUtils.getMyGroupList(mContext, user_id, role, type).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isEmpty) {
                    mView.showNoNet();
                }

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (isEmpty) {
                            mView.showNoNet();
                        }

                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (isEmpty) {
                            mView.showNoData();
                        }

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (classInfo.data != null && classInfo.data.getList() != null && classInfo.data.getList().size() > 0) {
                            mView.showUnionList1(classInfo.data.getList());
                            mView.hideStateView();
                            isEmpty = false;
                        } else {
                            mView.showNoData();
                        }

                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }


    public void getUnionList(String type, final int page, int page_size, final boolean isFitst) {
        if (page == 1 && isFitst) {
            mView.showLoading();
        }
        Subscription subscription = EngineUtils.getUnionList(mContext, type, page, page_size, UserInfoHelper.getUserInfo().getUid()).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1 && isFitst) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1 && isFitst) {
                            mView.showNoNet();
                        }

                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1 && isFitst) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
//                        if (page == 1 && isFitst) {
                        mView.hideStateView();
//                        }
                        mView.showUnionList(classInfo.data.getList(), page, isFitst);
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getCommonClassList() {
        mView.showLoading();
        Subscription subscription = EngineUtils.getCommonClassList(mContext).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
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
                            mView.showCommonClassList(data.getList());
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


    public void getMemberList(Context context, String class_id, String status, String master_id, String type) {
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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


    public void applyJoinGroup(final ClassInfo classInfo) {
        String name = "群";
        String masterName = "群主";
        if (classInfo.getType().equals("1")) {
            name = "公会";
            masterName = "会主";
        }

        mView.showLoadingDialog("正在申请加入，请稍候");
        final String finalName = name;
        final String finalMasterName = masterName;
        Subscription subscription = EngineUtils.applyJoinGroup(mContext, UserInfoHelper.getUserInfo().getUid(), classInfo.getGroupId() + "").subscribe(new Subscriber<ResultInfo<GroupApplyInfo>>() {
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
                                ToastUtils.showShort(String.format(mContext.getString(R.string.congratulation_join), finalName));
                                setMode(classInfo);
                                RongIMUtil.insertMessage("欢迎" + UserInfoHelper.getUserInfo().getNickname() + "加入本群", classInfo.getClass_id());
                                RxBus.get().post(BusAction.GROUP_LIST, "from groupjoin");
                                StudentInfo studentInfo = new StudentInfo();
                                studentInfo.setUser_id(applyInfo.getUser_id());
                                studentInfo.setClass_id(applyInfo.getClass_id());
                                if (classInfo.getIs_allow_talk() == 0) {
                                    addForbidMember(studentInfo);
                                }

                            } else if (type == GroupConstant.CONDITION_VERIFY_JOIN) {

                                ToastUtils.showShort(String.format(mContext.getString(R.string.commit_apply_join), finalName, finalMasterName));
                            }

                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }


    public void isGroupMember(final ClassInfo classInfo) {
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


    public void getMemberList(String class_id, String status, String master_id, String type) {
        Subscription subscription = EngineUtils.getMemberList(mContext, class_id, status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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


    private void addForbidMember(StudentInfo studentInfo) {
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
        if (classInfo.getType().equals("1")) {//公会
            GroupApp.setMyExtensionModule(false, false);
        } else {
            GroupApp.setMyExtensionModule(false, true);
        }
        RongIM.getInstance().startGroupChat(mContext, classInfo.getClass_id(), classInfo.getClassName());
    }
}
