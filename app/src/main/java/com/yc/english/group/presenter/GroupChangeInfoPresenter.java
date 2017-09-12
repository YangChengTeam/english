package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupChangeInfoContract;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/4 14:17.
 * 修改个人信息
 */

public class GroupChangeInfoPresenter extends BasePresenter<BaseEngin, GroupChangeInfoContract.View> implements GroupChangeInfoContract.Presenter {
    public GroupChangeInfoPresenter(Context context, GroupChangeInfoContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void changeGroupInfo(Context context, String class_id, String name, String face, String vali_type) {
        mView.showLoadingDialog("正在修改，请稍候！");
        Subscription subscription = EngineUtils.changeGroupInfo(context, class_id, name, face, vali_type,"").subscribe(new Subscriber<ResultInfo<RemoveGroupInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<RemoveGroupInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showChangeResult(stringResultInfo.data);

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


}
