package com.yc.english.main.contract;

import com.yc.english.base.presenter.IPresenter;

import com.yc.english.base.view.IView;

/**
 * Created by zhangkai on 2017/8/1.
 */

public interface SplashContract {
    interface View extends IView {
        void gotToMain();
    }

    interface Presenter extends IPresenter {

    }
}
