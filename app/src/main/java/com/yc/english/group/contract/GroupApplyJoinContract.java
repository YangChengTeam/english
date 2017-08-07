package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

/**
 * Created by wanglin  on 2017/8/2 17:04.
 */

public interface GroupApplyJoinContract {

    interface View extends IView, IDialog {
        void showGroup(ClassInfo classInfo);

        void apply(int type);

    }

    interface Presenter extends IPresenter {
        void applyJoinGroup(String user_id, String sn);

        void queryGroupById(Context context,String id, String sn);
    }

}
