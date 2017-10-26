package com.yc.english.speak.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakEnglishBean;

import java.util.List;

/**
 * Created by wanglin  on 2017/10/13 08:59.
 */

public interface SpeakEnglishContract {

    interface View extends IView, ILoading, INoData, INoNet {

        void shoReadAndSpeakMorList(List<SpeakAndReadInfo> list, int page, boolean isFitst);

        void showSpeakEnglishDetail(List<SpeakEnglishBean> list);
    }

    interface Presenter extends IPresenter {
        void getListenEnglishDetail(String id);
    }
}
