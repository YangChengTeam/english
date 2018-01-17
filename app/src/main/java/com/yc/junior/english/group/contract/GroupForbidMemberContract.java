package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.RemoveGroupInfo;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.rong.models.GagGroupUser;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/30 10:02.
 */

public interface GroupForbidMemberContract {
    interface View extends IView, IDialog {

        void showRollBackResult(String[] userId, String nickName, String groupId, int position, boolean allForbid);

        void showLisGagUserResult(List<GagGroupUser> users, List<StudentInfo> list);

        void showForbidResult(StudentInfo studentInfo, boolean allForbid);

        void showChangeInfo(RemoveGroupInfo data, String is_allow_talk, String[] strs);
    }

    interface Presenter extends IPresenter {
        void addForbidMember(StudentInfo studentInfo, String minute, boolean flag);

        void rollBackMember(String[] userId, String nickName, String groupId, int position, boolean allForbid);

        void lisGagUser(String groupId, List<StudentInfo> list);
    }

}
