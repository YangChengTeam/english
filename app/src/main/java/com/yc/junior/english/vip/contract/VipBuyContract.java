package com.yc.junior.english.vip.contract;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.pay.alipay.OrderInfo;

import yc.com.base.IDialog;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2017/12/1 14:42.
 */

public interface VipBuyContract {

    interface View extends IView,IDialog,INoNet {

        void showOrderInfo(ResultInfo<OrderInfo> data, String s, String money);
        void shareAllow(Integer data);
    }

    interface Presenter extends IPresenter {
    }
}
