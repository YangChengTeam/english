package com.yc.junior.english.weixin.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

import java.util.List;

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
