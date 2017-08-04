package com.yc.english.setting.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;

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
