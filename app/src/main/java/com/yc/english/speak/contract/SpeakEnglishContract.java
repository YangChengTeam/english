package com.yc.english.speak.contract;

import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakEnglishBean;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2017/10/13 08:59.
 */

public interface SpeakEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet,IHide {

        void shoReadAndSpeakMorList(List<SpeakAndReadInfo> list, int page, boolean isFitst);

        void showSpeakEnglishDetail(List<SpeakEnglishBean> list);
    }

    interface Presenter extends IPresenter {
        void getListenEnglishDetail(String id);
    }
}
