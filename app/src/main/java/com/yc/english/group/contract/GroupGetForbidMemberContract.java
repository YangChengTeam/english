package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/31 09:20.
 */

public interface GroupGetForbidMemberContract {

    interface View extends IView, INoData, INoNet, ILoading {
        void showMemberList(List<StudentInfo> list);
    }

    interface Presenter extends IPresenter {
        void getMemberList(String sn, String status, String master_id, String flag);
    }
}
