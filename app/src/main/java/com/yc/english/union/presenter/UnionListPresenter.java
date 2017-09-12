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
import com.yc.english.main.model.domain.UserInfo;
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
        if (!forceUpdate) return;
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            getUnionList1(uid, "-1", "1");
            getMemberList(mContext, "", "0", uid);
        } else {
            mView.showUnionList1(null);
        }
    }

    public void getUnionList1(String user_id, String role, String type) {

        mView.showLoading();

        Subscription subscription = EngineUtils.getMyGroupList(mContext, user_id, role, type).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                mView.showNoNet();

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                        mView.showNoNet();

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                        mView.showNoData();

                    }

                    @Override
                    public void reulstInfoOk() {

                        mView.hideStateView();
                        mView.showUnionList1(classInfo.data.getList());


                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }


    public void getUnionList(String type, String flag, final int page, int page_size, final boolean isFitst) {
        if (page == 1 && isFitst) {
            mView.showLoading();
        }
        Subscription subscription = EngineUtils.getUnionList(mContext, type, flag, page, page_size, UserInfoHelper.getUserInfo().getUid()).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
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
                        if (page == 1 && isFitst) {
                            mView.hideStateView();
                        }
//                        mView.showUnionList(classInfo.data.getList(), page, isFitst);
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



}
