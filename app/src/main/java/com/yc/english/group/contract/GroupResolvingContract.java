package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.RemoveGroupInfo;

/**
 * Created by wanglin  on 2017/8/4 15:55.
 */

public interface GroupResolvingContract {

    interface View extends IView, IDialog {
        void showClassInfo(ClassInfo info);

        void showResolvingResult();
        void showChangeGroupInfo(RemoveGroupInfo data);
    }

    interface Presenter extends IPresenter {
        void resolvingGroup(String class_id, String master_id);

        void queryGroupById(Context context, String id);

        void changeGroupInfo(Context context, String class_id, String name, String face, String vali_type);
    }

}
