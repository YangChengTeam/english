package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/11 15:42.
 */

public interface GroupCommonClassContract {
    interface View extends IView ,IDialog,ILoading,INoData,INoNet{
        void showCommonClassList(List<ClassInfo> list);

        void showIsMember(int is_member);

        void showVerifyResult(List<StudentInfo> list);
    }

    interface Presenter extends IPresenter {
        void  getCommonClassList();
        void applyJoinGroup(ClassInfo classInfo);
        void isGroupMember(String class_id, String user_id);
        void getMemberList(String class_id, String status, String master_id,String flag);
    }

}
