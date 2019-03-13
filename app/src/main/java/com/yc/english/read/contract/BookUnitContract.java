package com.yc.english.read.contract;

import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.UnitInfoList;

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

public interface BookUnitContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet ,IHide{
        void showBookInfo(BookInfo bookInfo);

        void showBookUnitListData(UnitInfoList unitInfoList);
    }

    interface Presenter extends IPresenter {
        void getBookInfoById(String bookId);

        void bookUnitInfo(int currentPage, int pageCount, String bookId);//获取单元集合
    }
}
