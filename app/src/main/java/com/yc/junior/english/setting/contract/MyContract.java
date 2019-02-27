package com.yc.junior.english.setting.contract;

import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.model.bean.MyOrderInfo;
import com.yc.junior.english.setting.model.bean.ScoreInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface MyContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet,IHide {
        void showUserInfo(UserInfo userInfo);

        void showNoLogin(Boolean flag);

        void showMyOrderInfoList(List<MyOrderInfo> list);

        void showScoreResult(ScoreInfo data);
    }

    interface Presenter extends IPresenter {
        void getUserInfo();

        void getMyOrderInfoList(int page, int limit);
    }
}
