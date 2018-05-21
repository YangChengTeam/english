package com.yc.junior.english.group.contract;

import android.content.Context;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/3 16:48.
 */

public interface GroupMyMemberListContract {
    interface View extends IView, ILoading, INoNet, INoData,IDialog,IFinish {
        void showMemberList(List<StudentInfo> list);

    }

    interface Presenter extends IPresenter {
        void getMemberList(Context context, String class_id,int page,int page_size, String status, String master_id,String flag);

        void exitGroup(String class_id, String master_id, String members);

    }

}
