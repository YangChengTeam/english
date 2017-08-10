package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/8 17:36.
 */

public interface GroupDoTaskContract {
    interface View extends IView {
    }

    interface Presenter extends IPresenter {

        void doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs);
    }
}
