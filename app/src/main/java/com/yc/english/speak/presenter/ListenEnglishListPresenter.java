package com.yc.english.speak.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.speak.contract.ListenEnglishContract;
import com.yc.english.speak.model.bean.ListenEnglishBean;
import com.yc.english.speak.model.engine.ListenEnglishEngin;

import rx.Subscriber;
import rx.Subscription;


public class ListenEnglishListPresenter extends BasePresenter<ListenEnglishEngin, ListenEnglishContract.View> implements ListenEnglishContract.Presenter {

    public ListenEnglishListPresenter(Context context, ListenEnglishContract.View view) {
        super(context, view);
        mEngin = new ListenEnglishEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }

    @Override
    public void getListenEnglish(String id, final int currentPage, int pageCount) {
        if (currentPage == 1 && mFirstLoad) {
            mView.showLoading();
        }
        Subscription subscribe = mEngin.getListenEnglish(id, currentPage, pageCount).subscribe(new Subscriber<ResultInfo<ListenEnglishBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (currentPage == 1 && mFirstLoad) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<ListenEnglishBean> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.hideStateView();
                        }

                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showListenEnglishList(null);
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscribe);
    }
}
