package com.yc.junior.english.main.contract;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface RegisterContract {
    interface View extends IView, IDialog, IFinish {
        void codeRefresh();
    }

    interface Presenter extends IPresenter {
        void sendCode(String mobile);
        void register(String mobile, String pwd, String code);
    }
}
