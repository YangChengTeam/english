package com.yc.english.main.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.UIUitls;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.group.utils.ConnectUtils;
import com.yc.english.main.contract.SplashContract;
import com.yc.english.main.model.engin.SplashEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashPresenter extends BasePresenter<SplashEngin, SplashContract.View> implements SplashContract.Presenter {
    public SplashPresenter(Context context, SplashContract.View view) {
        super(view);
        mContext = context;
        mEngin = new SplashEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void connect(String uid, final Runnable runnable) {
        Subscription subscription = mEngin.getTokenInfo(uid).subscribe(new Subscriber<ResultInfo<TokenInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<TokenInfo> tokenInfoResultInfo) {
                if (tokenInfoResultInfo.code == HttpConfig.STATUS_OK) {
                    UIUitls.post(new Runnable() {
                        @Override
                        public void run() {
                            ConnectUtils.contact(mContext, tokenInfoResultInfo.data);
                            if(runnable != null){
                                runnable.run();
                            }
                        }
                    });
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
