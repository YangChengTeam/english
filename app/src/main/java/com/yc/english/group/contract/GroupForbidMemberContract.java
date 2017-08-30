package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/30 10:02.
 */

public interface GroupForbidMemberContract {
    interface View extends IView, IDialog {
        void showForbidResult();

        void showRollBackResult();

    }

    interface Presenter extends IPresenter {
        void addForbidMember(String userId, String groupId, String minute);

        void rollBackMember(String[] userId, String groupId);
    }

}
