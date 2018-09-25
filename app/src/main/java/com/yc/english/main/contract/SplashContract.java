package com.yc.english.main.contract;

import com.yc.english.base.presenter.IPresenter;

import com.yc.english.base.view.IView;
import com.yc.english.main.model.domain.SlideInfo;

/**
 * Created by zhangkai on 2017/8/1.
 */

public interface SplashContract {
    interface View extends IView {
        void gotToMain(long l);

        void showAdvInfo(SlideInfo info, long delay);


    }

    interface Presenter extends IPresenter {
        void getDialogInfo();

        void getIndexMenuInfo();
    }
}
