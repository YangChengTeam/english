package com.yc.english.weixin.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.english.weixin.model.domain.WeiKeInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface WeiKeContract {
    interface View extends IView, ILoading, INoData, INoNet,IHide {
        void showWeikeCategoryList(WeiKeCategoryWrapper categoryWrapper);

        void showWeiKeInfoList(List<WeiKeInfo> list);

        void fail();

        void end();
    }

    interface Presenter extends IPresenter {
        void getWeikeCategoryList(String type,String page,String cate);

        void getWeiKeInfoList(String pid, String page);
    }
}
