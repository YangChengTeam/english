package com.yc.junior.english.setting.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.setting.model.bean.GoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/6 15:56.
 */

public interface GoodsListContract {

    interface View extends IView,ILoading,INoData,INoNet,IDialog{
        void showGoodVipList(List<GoodInfo> list);

        void showOrderInfo(OrderInfo data, String s, String money);
    }

    interface Presenter extends IPresenter{}
}
