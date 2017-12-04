package com.yc.english.news.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.OrderResultInfoHelper;
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
        mView.showLoadingDialog("正在下单， 请稍后");
        Subscription subscription = mEngin.createOrder(orderParams).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();

            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> resultInfo) {
                OrderResultInfoHelper.handleResultInfo(resultInfo, new OrderResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoIsBuy() {
                        mView.isBuy();
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

    @Override
    public void orderPay(String orderSn) {
        Subscription subscription = mEngin.orderPay(orderSn).subscribe(new Subscriber<ResultInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("orderPay error --->");
            }

            @Override
            public void onNext(final ResultInfo resultInfo) {
                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null) {
                            mView.showOrderPayResult(resultInfo);
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }
}
