package com.yc.junior.english.speak.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.speak.contract.ListenEnglishContract;
import com.yc.junior.english.speak.model.bean.ListenEnglishWarpper;
import com.yc.junior.english.speak.model.engine.ListenEnglishEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;


public class ListenEnglishPresenter extends BasePresenter<ListenEnglishEngin, ListenEnglishContract.View> implements ListenEnglishContract.Presenter {

    private Context mContext;

    public ListenEnglishPresenter(Context context, ListenEnglishContract.View view) {
        super(context, view);
        mContext = context;
        mEngine = new ListenEnglishEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }

    @Override
    public void getListenEnglishDetail(String id) {
        mView.showLoading();
        Subscription subscribe = mEngine.getListenReadDetail(mContext, id).subscribe(new Subscriber<ResultInfo<ListenEnglishWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<ListenEnglishWarpper> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void reulstInfoOk() {

                        if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null && resultInfo.data.info != null) {
                            mView.showListenEnglishDetail(resultInfo.data.info);
                            mView.hide();
                        } else {
                            mView.showNoData();
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscribe);
    }
}
