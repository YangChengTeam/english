package com.yc.english.speak.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.speak.model.bean.ListenEnglishBean;


public interface ListenEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet {

        void showListenEnglishDetail(ListenEnglishBean listenEnglishBean);
    }

    interface Presenter extends IPresenter {
        void getListenEnglishDetail(String id);
    }
}
