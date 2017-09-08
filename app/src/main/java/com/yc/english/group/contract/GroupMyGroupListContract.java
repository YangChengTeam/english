package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/3 12:04.
 */

public interface GroupMyGroupListContract {
    interface View extends IView,ILoading,INoData,INoNet {
        /**
         * 显示我的班群列表
         *
         * @param list
         */
        void showMyGroupList(List<ClassInfo> list);

        /**
         * 显示班群的成员列表
         *
         * @param count
         */
        void showMemberList(List<StudentInfo> count);


    }

    interface Presenter extends IPresenter {

        void getMyGroupList(Context context, String user_id, String is_admin,String type);

        void getMemberList(Context context, String class_id, String status, String master_id);
    }
}
