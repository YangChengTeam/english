package yc.com.base;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;

import androidx.annotation.NonNull;
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

    protected boolean mFirstLoad = true;

    public BasePresenter(V view) {
        this(null, view);
    }

    public BasePresenter(Context context, V view) {
        this.mContext = context;
        mSubscriptions = new CompositeSubscription();
        this.mView = view;
    }

    public void loadData(boolean isForceUI) {

        loadData(mFirstLoad | isForceUI, true);

        mFirstLoad = false;
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
