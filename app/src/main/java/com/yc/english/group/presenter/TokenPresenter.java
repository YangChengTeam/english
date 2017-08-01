package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.TokenContract;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.group.model.engin.TokenEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/7/31 15:04.
 */

public class TokenPresenter extends BasePresenter<TokenEngine, TokenContract.View> implements TokenContract.Presenter {

    public TokenPresenter(Context context, TokenContract.View view) {
        super(view);
        mEngin = new TokenEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getToken();
    }

    @Override
    public void getToken() {
        Subscription subscription = mEngin.getTokenInfo("5").subscribe(new Subscriber<ResultInfo<TokenInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<TokenInfo> tokenInfoResultInfo) {
                if (tokenInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    mView.contact(tokenInfoResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
