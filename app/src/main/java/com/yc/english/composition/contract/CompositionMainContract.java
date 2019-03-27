package com.yc.english.composition.contract;

import java.util.List;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/26 10:29.
 */
public interface CompositionMainContract {
    interface View extends IView {
        void showBanner(List<String> images);
    }

    interface Presenter extends IPresenter {
    }
}
