package com.yc.english.setting.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.main.model.domain.UserInfo;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface MyContract {
    interface View extends IView {
        void showUserInfo(UserInfo userInfo);
        void showNoLogin(Boolean flag);
    }

    interface Presenter extends IPresenter {
        void getUserInfo();
    }
}
