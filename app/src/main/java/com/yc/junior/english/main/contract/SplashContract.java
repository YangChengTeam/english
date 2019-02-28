package com.yc.junior.english.main.contract;

import com.yc.junior.english.main.model.domain.SlideInfo;

import yc.com.base.IPresenter;
import yc.com.base.IView;

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
