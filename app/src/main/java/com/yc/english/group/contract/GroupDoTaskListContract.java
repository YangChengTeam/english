package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/8 16:05.
 */

public interface GroupDoTaskListContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter {
        void getDoTaskList(String class_id, String user_id);
    }


}
