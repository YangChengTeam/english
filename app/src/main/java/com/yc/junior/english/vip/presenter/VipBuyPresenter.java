package com.yc.junior.english.vip.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.news.model.domain.OrderParams;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.vip.contract.VipBuyContract;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/12/1 14:40.
 */

public class VipBuyPresenter extends BasePresenter<BaseEngin, VipBuyContract.View> {
    public VipBuyPresenter(Context context, VipBuyContract.View view) {
        super(context, view);
        mEngin = new BaseEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    public void createOrder(final OrderParams orderParams) {
        mView.showLoadingDialog("创建订单中，请稍候...");
        Subscription subscription = EngineUtils.createOrder(mContext, orderParams.getTitle(), orderParams.getMoney(), orderParams.getMoney(), orderParams.getPayWayName(), orderParams.getGoodsList()).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> orderInfoResultInfo) {
                mView.dismissLoadingDialog();
//                if (orderInfoResultInfo!= null){
                mView.showOrderInfo(orderInfoResultInfo, orderParams.getMoney(), orderParams.getTitle());
//                }

//                ResultInfoHelper.handleResultInfo(orderInfoResultInfo, new ResultInfoHelper.Callback() {
//                    @Override
//                    public void resultInfoEmpty(String message) {
//
//                        TipsHelper.tips(mContext,message);
//                    }
//
//                    @Override
//                    public void resultInfoNotOk(String message) {
//                        mView.dismissLoadingDialog();
//                        TipsHelper.tips(mContext,message);
//                    }
//
//                    @Override
//                    public void reulstInfoOk() {
//                        mView.dismissLoadingDialog();
//
//                    }
//                });

            }
        });
        mSubscriptions.add(subscription);
    }


    public void getShareVipAllow(String userId) {

        Subscription subscription = EngineUtils.getShareVipAllow(mContext, userId).subscribe(new Subscriber<ResultInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo shareResult) {
                ResultInfoHelper.handleResultInfo(shareResult, new ResultInfoHelper.Callback() {
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
                        mView.shareAllow();
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

}
