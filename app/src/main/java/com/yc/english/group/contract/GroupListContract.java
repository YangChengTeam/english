package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/31 17:44.
 */

public interface GroupListContract {
    interface View extends IView {
        void showGroupList(List<ClassInfo> classInfos);
    }

    interface Presenter extends IPresenter {
        void getGroupList();
    }

}
