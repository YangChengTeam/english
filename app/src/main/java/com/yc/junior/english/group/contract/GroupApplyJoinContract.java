package com.yc.junior.english.group.contract;

import android.content.Context;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.ClassInfo;
import com.yc.junior.english.group.model.bean.StudentInfo;

import java.util.List;

import io.rong.imlib.model.UserInfo;

/**
 * Created by wanglin  on 2017/8/2 17:04.
 */

public interface GroupApplyJoinContract {

    interface View extends IView, IDialog, IFinish {
        void showGroup(ClassInfo classInfo);

        void showMemberList(List<UserInfo> list, List<StudentInfo> dataList);
    }

    interface Presenter extends IPresenter {
        void applyJoinGroup(ClassInfo classInfo);

        void queryGroupById(Context context, String id, String sn);

        void getMemberList(String sn, String status,int page,int page_size, String master_id, String flag);

        void addForbidMember(StudentInfo studentInfo);
    }

}
