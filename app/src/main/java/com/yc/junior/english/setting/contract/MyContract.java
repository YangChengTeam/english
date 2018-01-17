package com.yc.junior.english.setting.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.model.bean.MyOrderInfo;
import com.yc.junior.english.setting.model.bean.ScoreInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface MyContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
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
