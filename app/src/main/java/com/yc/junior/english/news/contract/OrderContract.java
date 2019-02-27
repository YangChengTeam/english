package com.yc.junior.english.news.contract;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.view.IBuy;
import com.yc.junior.english.news.model.domain.OrderParams;
import com.yc.junior.english.pay.alipay.OrderInfo;

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
