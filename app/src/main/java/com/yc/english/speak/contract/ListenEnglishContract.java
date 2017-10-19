package com.yc.english.speak.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.speak.model.bean.ListenEnglishBean;

import java.util.List;


public interface ListenEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet {

        void showListenEnglishList(List<ListenEnglishBean> list);
    }

    interface Presenter extends IPresenter {
        void getListenEnglish(String id,int page ,int count);
    }
}
