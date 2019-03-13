package com.yc.english.read.contract;

import com.yc.english.read.model.domain.BookInfo;

import java.util.ArrayList;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface BookContract {
    interface View extends IView, IDialog, IFinish, ILoading,IHide {
        void showBookListData(ArrayList<BookInfo> list,boolean isEdit);

        void addBook(BookInfo bookInfo);

        void deleteBookRefresh();
    }

    interface Presenter extends IPresenter {
        void bookList(int currentPage, int pageCount, int type);//获取教材集合

        void addBook(BookInfo bookInfo);

        void deleteBook(BookInfo bookInfo);
    }
}
