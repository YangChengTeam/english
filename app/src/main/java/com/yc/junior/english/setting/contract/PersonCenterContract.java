package com.yc.junior.english.setting.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.main.model.domain.UserInfo;

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
