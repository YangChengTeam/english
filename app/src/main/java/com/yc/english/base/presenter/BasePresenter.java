package com.yc.english.base.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.view.IView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangkai on 2017/7/20.
 */

public abstract class BasePresenter<M, V extends IView> implements IPresenter {
    @NonNull
    protected CompositeSubscription mSubscriptions;
    protected boolean mFirstLoad = true;

    protected M mEngin;
    protected V mView;
    protected Context mContext;

    public BasePresenter(V v) {
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

    public String getMessage(String message, String desc) {
        return EmptyUtils.isEmpty(message) ? desc : message;
    }

    public <T> void handleResultInfo(ResultInfo<T> resultInfo, Runnable runnable) {
        if (EmptyUtils.isEmpty(resultInfo)) {
            ToastUtils.showShort(HttpConfig.SERVICE_ERROR);
            return;
        }

        if (resultInfo.code != HttpConfig.STATUS_OK) {
            ToastUtils.showShort(getMessage(resultInfo.message, HttpConfig.NET_ERROR));
        } else {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public <T> void handleResultInfo(ResultInfo<T> resultInfo) {
        handleResultInfo(resultInfo, null);
    }


}

