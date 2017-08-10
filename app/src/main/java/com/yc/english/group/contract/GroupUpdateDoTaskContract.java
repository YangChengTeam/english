package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/8 17:46.
 */

public interface GroupUpdateDoTaskContract {
    interface View extends IView{}
    interface Presenter extends IPresenter{
        void updateDoTask(String id, String user_id, String desp, String imgs, String voices, String docs);
    }
}
