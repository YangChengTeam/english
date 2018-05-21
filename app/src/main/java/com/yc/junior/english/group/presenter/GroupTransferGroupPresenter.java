package com.yc.junior.english.group.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.contract.GroupTransferGroupContract;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.model.engin.GroupTransferGroupEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/4 19:33.
 */

public class GroupTransferGroupPresenter extends BasePresenter<GroupTransferGroupEngine, GroupTransferGroupContract.View> implements GroupTransferGroupContract.Presenter {
    public GroupTransferGroupPresenter(Context context, GroupTransferGroupContract.View view) {
        super(context, view);
        mEngin = new GroupTransferGroupEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    @Override
    public void transferGroup(String class_id, String master_id, String user_name) {

        if (TextUtils.isEmpty(user_name)) {
            TipsHelper.tips(mContext, GroupInfoHelper.getClassInfo().getType().equals("1") ?
                    mContext.getResources().getString(R.string.receive_union_phone) : mContext.getResources().getString(R.string.receive_group_phone));
            return;
        }
        mView.showLoadingDialog("正在" + (GroupInfoHelper.getClassInfo().getType().equals("1") ?
                mContext.getResources().getString(R.string.transfer_union) : mContext.getResources().getString(R.string.transfer_group)) + "，请稍候");
        Subscription subscription = mEngin.transferGroup(class_id, master_id, user_name).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.dismissLoadingDialog();

                        RxBus.get().post(BusAction.FINISH, BusAction.REMOVE_GROUP);
                        RxBus.get().post(BusAction.GROUP_LIST, "transfer group");
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
