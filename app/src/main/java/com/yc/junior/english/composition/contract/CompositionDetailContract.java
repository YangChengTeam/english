package com.yc.junior.english.composition.contract;

import com.yc.junior.english.composition.model.bean.CompositionDetailInfo;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/26 14:29.
 */
public interface CompositionDetailContract {
    interface View extends IView, ILoading, INoNet, INoData, IHide {
        void showCompositionDetailInfo(CompositionDetailInfo info);
    }

    interface Presenter extends IPresenter {
    }
}
