package com.yc.junior.english.main.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;

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
