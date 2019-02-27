package com.yc.junior.english.read.contract;

import com.yc.junior.english.read.model.domain.WordInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface ReadWordContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet,IHide {
        void showWordListData(List<WordInfo> list);
    }

    interface Presenter extends IPresenter {
        void getWordListByUnitId(int currentPage, int pageCount,String unitId);//获取单词集合
    }
}
