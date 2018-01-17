package com.yc.junior.english.vip.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2017/12/1 14:42.
 */

public interface VipBuyContract {

    interface View extends IView,IDialog {

        void showOrderInfo(OrderInfo data, String s, String money);
    }

    interface Presenter extends IPresenter {
    }
}
