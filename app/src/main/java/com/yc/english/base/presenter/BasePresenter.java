package com.yc.english.base.presenter;

import android.support.annotation.NonNull;

import com.kk.securityhttp.engin.BaseEngin;
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

    public BasePresenter(V v){
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

}

