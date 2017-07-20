package com.yc.english.contract;

import com.yc.base.presenter.IPresenter;
import com.yc.base.view.IView;

/**
 * Created by zhangkai on 2017/7/20.
 */

public interface MainContract {
    interface View extends IView<Presenter> {
        void show(String html);
    }
    interface Presenter extends IPresenter {
        void loadData(boolean forceUpdate);
    }
}
