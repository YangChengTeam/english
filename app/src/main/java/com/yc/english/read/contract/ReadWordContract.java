package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.WordInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface ReadWordContract {
    interface View extends IView, IDialog, IFinish {
        void showWordListData(List<WordInfo> list);
    }

    interface Presenter extends IPresenter {
        void getWordListByUnitId(int currentPage, int pageCount,String unitId);//获取单词集合
    }
}
