package com.yc.english.composition.contract;

import com.yc.english.composition.model.bean.CompositionInfo;
import com.yc.english.composition.model.bean.CompositionInfoWrapper;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/23 09:13.
 */
public interface EssayContract {
    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showCompositionInfos(List<CompositionInfo> compositionInfos);

        void showCompositionIndexInfo(CompositionInfoWrapper data);

        void showBanner(List<String> images);
    }

    interface Presenter extends IPresenter {
        void getCompositionIndexInfo();
    }
}
