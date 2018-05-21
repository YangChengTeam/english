package com.yc.junior.english.group.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.TaskAllInfoWrapper;

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
