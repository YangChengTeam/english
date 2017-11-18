package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;

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
