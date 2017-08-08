package com.yc.english.read.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.BookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.Constant;
import com.yc.english.read.model.engin.BookEngin;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class BookPresenter extends BasePresenter<BookEngin, BookContract.View> implements BookContract.Presenter {

    public BookPresenter(Context context, BookContract.View view) {
        super(context, view);
        mEngin = new BookEngin(context);
    }

    @Override
    public void bookList(int currentPage, int pageCount, int type) {

        Subscription subscribe = mEngin.bookList(currentPage, pageCount, type).subscribe(new Subscriber<List<BookInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<BookInfo> bookInfos) {
                if (bookInfos != null && bookInfos.size() > 0) {
                    RxBus.get().post(Constant.BOOK_INFO_LIST, bookInfos);
                } else {
                    mView.showBookListData(null);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
