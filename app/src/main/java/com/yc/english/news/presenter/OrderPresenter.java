package com.yc.english.news.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.news.contract.OrderContract;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.news.model.engin.OrderEngin;
import com.yc.english.pay.alipay.OrderInfo;

import rx.Subscriber;
import rx.Subscription;

public class OrderPresenter extends BasePresenter<OrderEngin, OrderContract.View> implements OrderContract.Presenter {
    public OrderPresenter(Context context, OrderContract.View view) {
        super(context, view);
        mEngin = new OrderEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void createOrder(OrderParams orderParams) {
        mView.showLoading();
        Subscription subscription = mEngin.createOrder(orderParams).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> resultInfo) {
                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo.data != null) {
                            mView.showOrderInfo(resultInfo.data);
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }
}
