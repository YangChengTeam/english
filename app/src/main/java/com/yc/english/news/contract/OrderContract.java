package com.yc.english.news.contract;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.view.IBuy;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.alipay.OrderInfo;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * 创建订单
 */

public interface OrderContract {
    interface View extends IView, IDialog,IBuy {
        void showOrderInfo(OrderInfo orderInfo);

        void showOrderPayResult(ResultInfo resultInfo);
    }

    interface Presenter extends IPresenter {
        void createOrder(OrderParams orderParams);

        void orderPay(String orderSn);
    }
}
