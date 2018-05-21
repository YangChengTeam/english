package com.yc.junior.english.speak.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.speak.model.bean.ListenEnglishBean;


public interface ListenEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet {

        void showListenEnglishDetail(ListenEnglishBean listenEnglishBean);
    }

    interface Presenter extends IPresenter {
        void getListenEnglishDetail(String id);
    }
}
