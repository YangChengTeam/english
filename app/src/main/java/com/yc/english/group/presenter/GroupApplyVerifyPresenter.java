package com.yc.english.group.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupApplyVerifyContract;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupApplyVerifyEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import rx.Subscriber;
import rx.Subscription;

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
        String uid = UserInfoHelper.getUserInfo().getUid();
        getMemberList(mContext, "", "0", uid);

    }

    /**
     * 获取申请加群列表
     *
     * @param class_id
     * @param status
     */
    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
     * @param class_id
     * @param master_id
     * @param user_ids
     */
    @Override
    public void acceptApply(String class_id, String master_id, String user_ids) {
        Subscription subscription = mEngin.acceptApply(class_id, master_id, user_ids).subscribe(new Subscriber<ResultInfo<String>>() {
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
                        RxBus.get().post(BusAction.GROUP_LIST, "join Group");
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


}
