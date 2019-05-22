package com.yc.english.vip.contract;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/5/21 16:43.
 */
public interface BindPhoneContract {

    interface View extends IView,IDialog {
        void showBindSuccess();

        void senCodeSuccess();
    }

    interface Presenter extends IPresenter {
        void bindPhone(String phone, String code, String pwd,String resetPwd);
    }
}
