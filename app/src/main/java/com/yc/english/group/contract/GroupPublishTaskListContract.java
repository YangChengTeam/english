package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/8 15:57.
 */

public interface GroupPublishTaskListContract {
    interface View extends IView,ILoading,INoData,INoNet{
        void showPublishTaskList(List<TaskAllInfoWrapper.TaskAllInfo> list);
    }
    interface Presenter extends IPresenter{
        void getPublishTaskList(String publisher, String class_id);
    }
}
