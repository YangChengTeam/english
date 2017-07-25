package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/7/24 18:44.
 */

public interface GroupCreateContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter {
        void createGroup();
    }

}
