package com.yc.junior.english.main.contract;

import com.yc.junior.english.base.presenter.IPresenter;

import com.yc.junior.english.base.view.IView;

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
