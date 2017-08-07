package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/3 16:48.
 */

public interface GroupMyMemberListContract {
    interface View extends IView {
        void showMemberList(List<StudentInfo> list);

        void showGroupInfo(ClassInfo info);
    }

    interface Presenter extends IPresenter {
        void getMemberList(Context context, String class_id, String status, String master_id);
    }

}
