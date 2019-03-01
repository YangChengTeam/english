package com.yc.english.setting.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.main.model.domain.UserInfo;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface PersonCenterContract {
    interface View extends IView, IDialog, IFinish {
        void showUserInfo(UserInfo userInfo);
    }

    interface Presenter extends IPresenter {
        void getUserInfo();
        void uploadAvatar(String avatar);
    }
}
