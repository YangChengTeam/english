package com.yc.junior.english.speak.contract;

import com.yc.junior.english.speak.model.bean.ListenEnglishBean;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;


public interface ListenEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet,IHide {

        void showListenEnglishDetail(ListenEnglishBean listenEnglishBean);
    }

    interface Presenter extends IPresenter {
        void getListenEnglishDetail(String id);
    }
}
