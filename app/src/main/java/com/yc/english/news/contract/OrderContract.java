package com.yc.english.news.contract;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.alipay.OrderInfo;

/**
 * 创建订单
 */

public interface OrderContract {
    interface View extends IView, ILoading, INoData, INoNet, IDialog {
        void showOrderInfo(OrderInfo orderInfo);

        void showOrderPayResult(ResultInfo resultInfo);
    }

    interface Presenter extends IPresenter {
        void createOrder(OrderParams orderParams);

        void orderPay(String orderSn);
    }
}
