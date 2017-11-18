package com.yc.english.union.contract;

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
 * Created by wanglin  on 2017/9/7 12:23.
 */

public interface UnionCommonListContract {
    interface View extends IView, ILoading, INoData, INoNet, IDialog {

        void showUnionList(List<ClassInfo> data, int page, boolean isFitst);

        void showMemberList(List<StudentInfo> list);

        void showMyGroupList(List<ClassInfo> list);

        void showCommonClassList(List<ClassInfo> list);

        void showIsMember(int is_member, ClassInfo classInfo);

    }

    interface Presenter extends IPresenter {
        void getCommonClassList();

        void applyJoinGroup(ClassInfo classInfo);

        void isGroupMember(ClassInfo classInfo);

    }
}
