package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;

/**
 * Created by wanglin  on 2017/7/24 18:44.
 */

public interface GroupCreateContract {
    interface View extends IView,IFinish, IDialog {
    }

    interface Presenter extends IPresenter {
        void createGroup(String user_id, String groupName, String face,String type);
    }

}
