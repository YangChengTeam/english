package com.yc.english.setting.contract;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/3.
 */

public interface SettingContract {
    interface View extends IView, IDialog {
        void ShowCacheSize(String size);
        void showExitButton();
    }

    interface Presenter extends IPresenter {
        void getCacheSize();
    }
}
