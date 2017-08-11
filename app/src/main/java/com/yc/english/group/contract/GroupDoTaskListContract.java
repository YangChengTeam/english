package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/8 16:05.
 */

public interface GroupDoTaskListContract {
    interface View extends IView {
        void showDoneTaskResult(List<TaskAllInfoWrapper.TaskAllInfo> list);
    }

    interface Presenter extends IPresenter {
        void getDoTaskList(String class_id, String user_id);
    }


}
