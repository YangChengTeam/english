package com.yc.junior.english.main.contract;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface LoginContract {
    interface View extends IView, IDialog, IFinish {
        void showPhone(String phone);
    }

    interface Presenter extends IPresenter {
        void login(String username, String pwd);
        void getPhone();
    }
}
