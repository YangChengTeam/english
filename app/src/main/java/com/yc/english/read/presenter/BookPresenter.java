package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.utils.UIUitls;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.BookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.engin.BookEngin;

import java.util.ArrayList;

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
        Subscription subscribe = mEngin.bookList(currentPage, pageCount, type).subscribe(new Subscriber<ArrayList<BookInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ArrayList<BookInfo> bookInfos) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showBookListData(bookInfos);
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }


    @Override
    public void addBook(BookInfo bookInfo) {
        Subscription subscribe = mEngin.addBook(bookInfo).subscribe(new Subscriber<ArrayList<BookInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ArrayList<BookInfo> bookInfos) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showBookListData(bookInfos);
                    }
                });
            }
        });
        mSubscriptions.add(subscribe);
    }

    @Override
    public void deleteBook(BookInfo bookInfo) {
        Subscription subscribe = mEngin.deleteBook(bookInfo).subscribe(new Subscriber<ArrayList<BookInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ArrayList<BookInfo> bookInfos) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showBookListData(bookInfos);
                    }
                });
            }
        });
        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        bookList(0, 0, 1);
    }
}
