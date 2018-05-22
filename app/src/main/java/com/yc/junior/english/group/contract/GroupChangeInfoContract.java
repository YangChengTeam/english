package com.yc.junior.english.group.contract;

import android.content.Context;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.RemoveGroupInfo;

/**
 * Created by wanglin  on 2017/8/4 14:33.
 */

public interface GroupChangeInfoContract {

    interface View extends IView, IDialog,IFinish {
        void showChangeResult(RemoveGroupInfo data, String vali_type);
    }

    interface Presenter extends IPresenter {
        void changeGroupInfo(Context context,String class_id, String name, String face, String vali_type);
    }
}
