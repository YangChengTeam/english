package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.RemoveGroupInfo;

/**
 * Created by wanglin  on 2017/8/4 14:33.
 */

public interface GroupChangeInfoContract {

    interface View extends IView, IDialog {
        void showChangeResult(RemoveGroupInfo data);
    }

    interface Presenter extends IPresenter {
        void changeGroupInfo(Context context,String class_id, String name, String face, String vali_type);
    }
}
