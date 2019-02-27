package com.yc.junior.english.setting.contract;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface NameSettingContract {
    interface View extends IView, IDialog, IFinish {

    }

    interface Presenter extends IPresenter {
        void udpateUserInfo(String name, String school);
    }
}
