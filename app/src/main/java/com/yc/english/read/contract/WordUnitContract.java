package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.WordUnitInfo;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface WordUnitContract {
    interface View extends IView, IDialog, IFinish {
        void showWordUnitListData(WordUnitInfo wordUnitInfo);
    }

    interface Presenter extends IPresenter {
        void bookUnitInfo(int currentPage, int pageCount);//获取教材集合
    }
}
