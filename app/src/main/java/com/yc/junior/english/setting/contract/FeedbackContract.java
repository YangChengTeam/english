package com.yc.junior.english.setting.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IView;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface FeedbackContract {
    interface View extends IView, IDialog {
        void emptyView();
    }

    interface Presenter extends IPresenter {
        void postMessage(String message);
    }
}
