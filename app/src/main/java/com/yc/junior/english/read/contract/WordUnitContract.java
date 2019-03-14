package com.yc.junior.english.read.contract;

import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.WordUnitInfoList;

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

public interface WordUnitContract {
    interface View extends IView, IDialog, IFinish, INoData, INoNet, ILoading, IHide {
        void showBookInfo(BookInfo bookInfo);

        void showWordUnitListData(WordUnitInfoList wordUnitInfoList);
    }

    interface Presenter extends IPresenter {
        void getBookInfoById(String bookId);

        void getWordUnitByBookId(int currentPage, int pageCount, String bookId);//根据课本ID获取单词对应的单元列表集合
    }
}
