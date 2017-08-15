package com.yc.english.group.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.GroupApplyInfo;
import com.yc.english.group.model.engin.GroupCommonClassEngine;
import com.yc.english.group.utils.EngineUtils;

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
                handleResultInfo(classInfoListResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (classInfoListResultInfo.data.getList() != null && classInfoListResultInfo.data.getList().size() > 0) {
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
}
