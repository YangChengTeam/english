package com.yc.english.setting.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.model.bean.PayWayInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/6 15:56.
 */

public interface GoodsListContract {

    interface View extends IView,ILoading,INoData,INoNet{
        void showGoodVipList(List<GoodInfo> list);

        void showPayWayList(List<PayWayInfo> data);
    }

    interface Presenter extends IPresenter{}
}
