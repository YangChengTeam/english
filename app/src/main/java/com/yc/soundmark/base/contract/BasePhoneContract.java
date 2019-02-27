package com.yc.soundmark.base.contract;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/11/1 15:04.
 */
public interface BasePhoneContract {

    interface View extends IView,IDialog {
        void showUploadSuccess();
    }

    interface Presenter extends IPresenter {
    }
}
