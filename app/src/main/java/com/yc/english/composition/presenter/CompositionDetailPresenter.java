package com.yc.english.composition.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.composition.contract.CompositionDetailContract;
import com.yc.english.composition.contract.CompositionMainContract;
import com.yc.english.composition.model.bean.CompositionDetailInfoWrapper;
import com.yc.english.composition.model.engine.CompositionDetailEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2019/3/26 14:29.
 */
public class CompositionDetailPresenter extends BasePresenter<CompositionDetailEngine, CompositionDetailContract.View> implements CompositionMainContract.Presenter {
    public CompositionDetailPresenter(Context context, CompositionDetailContract.View view) {

        super(context, view);
        mEngine = new CompositionDetailEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    public void getCompositionDetail(String zwid) {
        mView.showLoading();
        Subscription subscription = mEngine.getCompositionDetail(zwid).subscribe(new Subscriber<ResultInfo<CompositionDetailInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<CompositionDetailInfoWrapper> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null && stringResultInfo.data.getInfo() != null) {
                        mView.hide();
                        mView.showCompositionDetailInfo(stringResultInfo.data.getInfo());
                    } else {
                        mView.showNoData();
                    }
                } else {
                    mView.showNoNet();
                }

            }
        });
        mSubscriptions.add(subscription);
    }
}
