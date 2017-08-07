package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2017/8/4 14:33.
 */

public interface GroupChangeInfoContract {

    interface View extends IView {
    }

    interface Presenter extends IPresenter {
        void changeGroupInfo(String class_id, String name, String face, String vali_type);
    }
}
