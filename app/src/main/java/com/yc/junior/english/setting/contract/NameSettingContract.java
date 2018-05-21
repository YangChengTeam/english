package com.yc.junior.english.setting.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;

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
