package com.yc.junior.english.read.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.UnitInfoList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface BookUnitContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
        void showBookInfo(BookInfo bookInfo);

        void showBookUnitListData(UnitInfoList unitInfoList);
    }

    interface Presenter extends IPresenter {
        void getBookInfoById(String bookId);

        void bookUnitInfo(int currentPage, int pageCount, String bookId);//获取单元集合
    }
}
