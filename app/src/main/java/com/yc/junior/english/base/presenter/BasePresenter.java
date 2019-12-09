package com.yc.junior.english.base.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.helper.TipsHelper;

import androidx.annotation.NonNull;
import rx.subscriptions.CompositeSubscription;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/20.
 */

public abstract class BasePresenter<M, V extends IView> implements IPresenter {
    @NonNull
    protected CompositeSubscription mSubscriptions;
    protected boolean mFirstLoad = true;

    protected M mEngine;
    protected V mView;
    protected Context mContext;
    protected boolean cached;

    public BasePresenter(V v) {
       this(null, v);
    }

    public BasePresenter(Context context, V v) {
        this.mContext = context;
        mSubscriptions = new CompositeSubscription();
        mView = v;
    }

    @Override
    public void subscribe() {
        loadData(false);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    public void loadData(boolean forceUpdate) {
        loadData(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public abstract void loadData(final boolean forceUpdate, final boolean showLoadingUI);


    public <T> void handleResultInfo(final ResultInfo<T> resultInfo, final Runnable runnable) {
        ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
            @Override
            public void resultInfoEmpty(String message) {
                TipsHelper.tips(mContext, message);
            }

            @Override
            public void resultInfoNotOk(String message) {
                TipsHelper.tips(mContext, message);
            }

            @Override
            public void reulstInfoOk() {
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }

    public <T> void handleResultInfo(ResultInfo<T> resultInfo) {
        handleResultInfo(resultInfo, null);
    }
}

