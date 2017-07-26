package com.yc.english.main.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface IndexContract {
    interface View extends IView {
        void showBanner(List<String> images);
    }
    interface Presenter extends IPresenter {
        void loadData();
    }
}
