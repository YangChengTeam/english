package com.yc.junior.english.read.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.WordUnitInfoList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface WordUnitContract {
    interface View extends IView, IDialog, IFinish, INoData, INoNet, ILoading {
        void showBookInfo(BookInfo bookInfo);
        void showWordUnitListData(WordUnitInfoList wordUnitInfoList);
    }

    interface Presenter extends IPresenter {
        void getBookInfoById(String bookId);
        void getWordUnitByBookId(int currentPage, int pageCount, String bookId);//根据课本ID获取单词对应的单元列表集合
    }
}
