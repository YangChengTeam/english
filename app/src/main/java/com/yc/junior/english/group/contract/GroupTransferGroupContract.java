package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/4 19:43.
 */

public interface GroupTransferGroupContract {
    interface View extends IView, IDialog,IFinish {
    }

    interface Presenter extends IPresenter {
        void transferGroup(String class_id, String master_id, String user_name);
    }

}
