package com.yc.english.main.contract;

import com.yc.english.base.presenter.IPresenter;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface ForgotContract {

    interface View extends IView, IFinish, IDialog {
        void codeRefresh();
    }

    interface Presenter extends IPresenter {
        void sendCode(String mobile);
        void resetPassword(String mobile, String pwd, String code);
    }
}
