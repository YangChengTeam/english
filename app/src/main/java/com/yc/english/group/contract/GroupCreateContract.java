package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

/**
 * Created by wanglin  on 2017/7/24 18:44.
 */

public interface GroupCreateContract {
    interface View extends IView,IFinish, IDialog {
        void showCreateResult(ClassInfo data);

    }

    interface Presenter extends IPresenter {
        void createGroup(String user_id, String groupName, String face);
    }

}
