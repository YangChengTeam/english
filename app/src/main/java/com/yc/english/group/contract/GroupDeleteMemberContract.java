package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/2 19:40.
 */

public interface GroupDeleteMemberContract {

    interface View extends IView,IDialog,ILoading,INoData,INoNet {
        void showMemberList(List<StudentInfo> list);

        void showDeleteResult();
    }

    interface Presenter extends IPresenter {
        void deleteMember(String class_id, String master_id, String[] members);

        void getMemberList(Context context, String class_id, String status, String master_id);
    }

}
