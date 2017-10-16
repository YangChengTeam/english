package com.yc.english.speak.presenter;

import android.content.Context;

import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.speak.contract.SpeakEnglishContract;
import com.yc.english.speak.model.bean.EnglishInfoWrapper;
import com.yc.english.speak.model.engine.SpeakEnglishListEngine;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/10/13 08:59.
 */

public class SpeakEnglishListPresenter extends BasePresenter<SpeakEnglishListEngine, SpeakEnglishContract.View> implements SpeakEnglishContract.Presenter {
    public SpeakEnglishListPresenter(Context context, SpeakEnglishContract.View view) {
        super(context, view);
        mEngin = new SpeakEnglishListEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getEnglishInfoList("");
    }


    @Override
    public void getEnglishInfoList(String type) {
        mView.showLoading();
        Subscription subscription = mEngin.getEnglishInfoList(type).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EnglishInfoWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(EnglishInfoWrapper englishInfoWrapper) {
                if (englishInfoWrapper.getCode() == HttpConfig.STATUS_OK && englishInfoWrapper.getData() != null) {
                    mView.hideStateView();
                    mView.showEnglishInfoList(englishInfoWrapper.getData().getList());
                } else {
                    mView.showNoData();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
