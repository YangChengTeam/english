package com.yc.english.group.contract;

import android.app.Activity;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

/**
 * Created by wanglin  on 2017/8/2 10:50.
 */

public interface GroupJoinContract {
    interface View extends IView,IFinish, IDialog {
        void  startGroupChat(String groupId, String groupName);
    }


    interface Presenter extends IPresenter {
        void joinGroup(String user_id, String groupName);
    }
}
