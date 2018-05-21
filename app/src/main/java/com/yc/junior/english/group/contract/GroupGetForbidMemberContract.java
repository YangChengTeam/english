package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.rong.models.GagGroupUser;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/31 09:20.
 */

public interface GroupGetForbidMemberContract {

    interface View extends IView, INoData, INoNet, ILoading {

        void showGagUserResult(List<GagGroupUser> users, List<StudentInfo> list);
    }

    interface Presenter extends IPresenter {
        void getMemberList(String sn, int page, int page_size, String status, String master_id, String flag);

        void lisGagUser(String groupId, List<StudentInfo> list);

    }
}
