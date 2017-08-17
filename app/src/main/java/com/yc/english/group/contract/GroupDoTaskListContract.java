package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/8 16:05.
 */

public interface GroupDoTaskListContract {
    interface View extends IView, ILoading, INoNet, INoData {
        void showDoneTaskResult(List<TaskAllInfoWrapper.TaskAllInfo> list);
    }

    interface Presenter extends IPresenter {
        void getDoTaskList(String class_id, String user_id);

        void getPublishTaskList(String publisher, String class_id);
    }


}
