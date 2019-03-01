package com.yc.english.weixin.contract;

import com.yc.english.base.presenter.IPresenter;

import java.util.List;

import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface CourseTypeContract {

    interface View extends IView {
        void showBanner(List<String> images);
    }

    interface Presenter extends IPresenter {
    }
}
