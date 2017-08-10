package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/8 16:23.
 */

public interface GroupUpdatePublishTaskContract {
    interface View extends IView{}
    interface Presenter extends IPresenter{
        void updatePublishTask(String id, String publisher, String desp, String imgs, String voices, String docs);
    }
}
