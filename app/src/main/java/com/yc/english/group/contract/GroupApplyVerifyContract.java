package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/2 18:22.
 */

public interface GroupApplyVerifyContract {
    interface View extends IView, ILoading, INoData, INoNet {
        void showVerifyList(List<StudentInfo> list);

        void showApplyResult(String data, int position);
    }

    interface Presenter extends IPresenter {

        void acceptApply(StudentInfo studentInfo,int position);

        void addForbidMember(StudentInfo studentInfo);
    }
}
