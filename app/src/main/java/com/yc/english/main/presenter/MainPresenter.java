package com.yc.english.main.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.kk.utils.UIUitls;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.model.engin.MainEngin;

import rx.Subscriber;
import rx.Subscription;


/**
 * Created by zhangkai on 2017/7/20.
 */

public class MainPresenter extends BasePresenter<MainEngin, MainContract.View> implements MainContract.Presenter {

    public MainPresenter(Context context, MainContract.View view) {
        super(view);
        mEngin = new MainEngin(context);
    }

    public void loadData(final boolean forceUpdate, final boolean showLoadingUI) {
        if(!forceUpdate) return;

        Subscription subscription = mEngin.main().subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                LogUtils.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i(e);
            }

            @Override
            public void onNext(final String s) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!StringUtils.isEmpty(s)) {
                            mView.show(s);
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
