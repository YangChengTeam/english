package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/8 15:57.
 */

public interface GroupPublishTaskListContract {
    interface View extends IView{}
    interface Presenter extends IPresenter{
        void getPublishTaskList(String publisher, String class_id);
    }
}
