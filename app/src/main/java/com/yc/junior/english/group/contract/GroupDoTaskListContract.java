package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.TaskAllInfoWrapper;

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
