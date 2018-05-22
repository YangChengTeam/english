package com.yc.junior.english.read.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.read.model.domain.BookInfo;

import java.util.ArrayList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface BookContract {
    interface View extends IView, IDialog, IFinish, ILoading {
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
