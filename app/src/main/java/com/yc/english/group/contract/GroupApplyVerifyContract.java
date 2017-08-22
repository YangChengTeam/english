package com.yc.english.group.contract;

import android.content.Context;

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

        void showApplyResult(String data);
    }

    interface Presenter extends IPresenter {
        void getMemberList(Context context, String class_id, String status, String master_id,String flag);

        void acceptApply(String class_id, String master_id, String user_ids);
    }
}
