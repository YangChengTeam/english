package com.yc.soundmark.base.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.soundmark.base.contract.BasePayContract;
import com.yc.soundmark.base.model.domain.GoodInfo;
import com.yc.soundmark.base.model.domain.GoodInfoWrapper;
import com.yc.soundmark.base.utils.VipInfoHelper;


import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BaseEngine;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/10/30 14:47.
 */
public class BasePayPresenter extends BasePresenter<BaseEngine, BasePayContract.View> implements BasePayContract.Presenter {
    public BasePayPresenter(Context context, BasePayContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getVipInfos();
    }

    @Override
    public void createOrder(final String payway_name, final String money, final String goods_id, final String title, List<OrderGood> list) {
        mView.showLoadingDialog("正在创建订单，请稍候...");
        Subscription subscription = EngineUtils.createOrder(mContext, title, money, money, payway_name, list).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<OrderInfo> orderInfoResultInfo) {
                mView.dismissDialog();
                if (orderInfoResultInfo != null && orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                    OrderInfo orderInfo = orderInfoResultInfo.data;
                    orderInfo.setMoney(Float.parseFloat(money));
                    orderInfo.setName(title);
                    orderInfo.setGoodId(goods_id);
                    mView.showOrderInfo(orderInfo);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    public void isBindPhone() {
        Subscription subscription = com.yc.soundmark.study.utils.EngineUtils.isBindPhone(mContext).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    //绑定手机号
                    mView.showBindSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    private void getVipInfos() {

        Subscription subscription = com.yc.soundmark.study.utils.EngineUtils.getVipInfoList(mContext).subscribe(new Subscriber<ResultInfo<GoodInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<GoodInfoWrapper> vipInfoWrapperResultInfo) {
                if (vipInfoWrapperResultInfo != null && vipInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && vipInfoWrapperResultInfo.data != null) {
                    GoodInfoWrapper infoWrapper = vipInfoWrapperResultInfo.data;
                    List<GoodInfo> vip_list = infoWrapper.getVip_list();
                    mView.showVipInfoList(vip_list);
                    VipInfoHelper.setVipInfoList(vip_list);
                }

            }
        });

        mSubscriptions.add(subscription);


    }


}
