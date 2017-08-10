package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.BookInfo;

import java.util.ArrayList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface BookContract {
    interface View extends IView, IDialog, IFinish {
        void showBookListData(ArrayList<BookInfo> list);

        void addBook(BookInfo bookInfo);

        void deleteBookRefresh();
    }

    interface Presenter extends IPresenter {
        void bookList(int currentPage, int pageCount, int type);//获取教材集合

        void addBook(BookInfo bookInfo);

        void deleteBook(BookInfo bookInfo);
    }
}
