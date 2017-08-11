package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/11 15:42.
 */

public interface GroupCommonClassContract {
    interface View extends IView {
        void showCommonClassList(List<ClassInfo> list);
    }

    interface Presenter extends IPresenter {
        void  getCommonClassList();
    }

}
