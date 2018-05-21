package com.yc.junior.english.weixin.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.junior.english.weixin.model.domain.WeiKeInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface WeiKeContract {
    interface View extends IView, ILoading, INoData, INoNet {
        void showWeikeCategoryList(WeiKeCategoryWrapper categoryWrapper);

        void showWeiKeInfoList(List<WeiKeInfo> list);

        void fail();

        void end();
    }

    interface Presenter extends IPresenter {
        void getWeikeCategoryList(String page);

        void getWeiKeInfoList(String pid, String page);
    }
}
