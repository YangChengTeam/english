package yc.com.base;

import android.content.Context;
import android.support.annotation.NonNull;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by wanglin  on 2018/3/6 10:14.
 */

public abstract class BasePresenter<M, V extends IView> implements IPresenter {

    protected M mEngine;

    protected V mView;

    protected Context mContext;

    @NonNull
    protected CompositeSubscription mSubscriptions;

    private boolean isFirstLoad = true;

    public BasePresenter(V view) {
        this(null, view);
    }

    public BasePresenter(Context context, V view) {
        this.mContext = context;
        mSubscriptions = new CompositeSubscription();
        this.mView = view;
    }

    public void loadData(boolean isForceUI) {

        loadData(isFirstLoad | isForceUI, true);

        isFirstLoad = false;
    }


    public abstract void loadData(boolean isForceUI, boolean isLoadingUI);

    @Override
    public void subscribe() {
        loadData(false);
    }


    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }



}
