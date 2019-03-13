package com.yc.english.vip.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.vip.contract.VipBuyContract;
import com.yc.english.vip.model.bean.VipGoodInfo;
import com.yc.english.vip.model.bean.VipGoodInfoWrapper;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BaseEngine;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2017/12/1 14:40.
 */

public class VipBuyPresenter extends BasePresenter<BaseEngine, VipBuyContract.View> {
    public VipBuyPresenter(Context context, VipBuyContract.View view) {
        super(context, view);
        mEngine = new BaseEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getVidGoodInfos();
    }

    public void createOrder(final OrderParams orderParams) {
        mView.showLoadingDialog("创建订单中，请稍候...");
        Subscription subscription = EngineUtils.createOrder(mContext, orderParams.getTitle(), orderParams.getMoney(), orderParams.getMoney(), orderParams.getPayWayName(), orderParams.getGoodsList()).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> orderInfoResultInfo) {
                mView.dismissDialog();

                mView.showOrderInfo(orderInfoResultInfo, orderParams.getMoney(), orderParams.getTitle());

            }
        });
        mSubscriptions.add(subscription);
    }


    public void getShareVipAllow(String userId) {

        Subscription subscription = EngineUtils.getShareVipAllow(mContext, userId).subscribe(new Subscriber<ResultInfo<Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<Integer> shareResult) {
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
                        mView.shareAllow(shareResult.data);
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }


    public void getVidGoodInfos() {
        Subscription subscription = EngineUtils.getVipGoodInfos(mContext).subscribe(new Subscriber<ResultInfo<VipGoodInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<VipGoodInfoWrapper> vipGoodInfoWrapperResultInfo) {
                if (vipGoodInfoWrapperResultInfo != null && vipGoodInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && vipGoodInfoWrapperResultInfo.data != null) {
                    List<VipGoodInfo> list = vipGoodInfoWrapperResultInfo.data.getList();
                    mView.showVipGoodInfos(list);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


}
