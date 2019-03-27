package com.yc.english.composition.contract;

import com.yc.english.composition.model.bean.CompositionInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/26 17:23.
 */
public interface CompositionSearchContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showSearchResult(List<CompositionInfo> list);
    }

    interface Presenter extends IPresenter {
        void searchCompositionInfos(String title, String grade_id, String topic, String ticai, String type,int page,int pagesize);
    }
}
