package com.yc.junior.english.setting.contract;

import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.setting.model.bean.GoodInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2017/11/6 15:56.
 */

public interface GoodsListContract {

    interface View extends IView,ILoading,INoData,INoNet,IDialog,IHide {
        void showGoodVipList(List<GoodInfo> list);

        void showOrderInfo(OrderInfo data, String s, String money);
    }

    interface Presenter extends IPresenter {}
}
