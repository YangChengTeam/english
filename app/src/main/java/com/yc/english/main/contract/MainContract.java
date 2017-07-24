package com.yc.english.main.contract;


import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by zhangkai on 2017/7/20.
 */

public interface MainContract {
    interface View extends IView {
        void show(String html);
    }
    interface Presenter extends IPresenter {
        void loadData(boolean forceUpdate);
    }
}
