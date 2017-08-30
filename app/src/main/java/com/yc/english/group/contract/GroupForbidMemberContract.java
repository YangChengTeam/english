package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/30 10:02.
 */

public interface GroupForbidMemberContract {
    interface View extends IView, IDialog {
        void showForbidResult(String id, String userId, String groupId, boolean allForbid);

        void showRollBackResult(String nickName, String groupId, boolean allForbid);

        void showMemberList(List<StudentInfo> list);
    }

    interface Presenter extends IPresenter {
        void addForbidMember(String userId, String nickName, String groupId, String minute, boolean flag);

        void rollBackMember(String[] userId, String nickName, String groupId, boolean allForbid);
    }

}
