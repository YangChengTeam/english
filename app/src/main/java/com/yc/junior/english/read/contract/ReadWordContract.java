package com.yc.junior.english.read.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.read.model.domain.WordInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface ReadWordContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
        void showWordListData(List<WordInfo> list);
    }

    interface Presenter extends IPresenter {
        void getWordListByUnitId(int currentPage, int pageCount,String unitId);//获取单词集合
    }
}
