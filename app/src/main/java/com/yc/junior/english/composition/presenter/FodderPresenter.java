package com.yc.junior.english.composition.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.composition.contract.FodderContract;
import com.yc.junior.english.composition.model.bean.FodderInfo;
import com.yc.junior.english.composition.model.bean.FodderInfoWrapper;
import com.yc.junior.english.composition.model.engine.FodderEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2019/3/25 09:44.
 */
public class FodderPresenter extends BasePresenter<FodderEngine, FodderContract.View> implements FodderContract.Presenter {
    public FodderPresenter(Context context, FodderContract.View view) {
        super(context, view);
        mEngine = new FodderEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getFodderIndexInfo();
    }


    @Override
    public void getFodderIndexInfo() {
        Subscription subscription = mEngine.getFodderIndexInfo().subscribe(new Subscriber<ResultInfo<FodderInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<FodderInfoWrapper> fodderInfoWrapperResultInfo) {
                if (fodderInfoWrapperResultInfo != null && fodderInfoWrapperResultInfo.code == HttpConfig.STATUS_OK &&
                        fodderInfoWrapperResultInfo.data != null && fodderInfoWrapperResultInfo.data.getFodderInfos() != null) {
                    List<FodderInfo> fodderInfos = fodderInfoWrapperResultInfo.data.getFodderInfos();
                    mView.showFodderInfos(fodderInfos);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
