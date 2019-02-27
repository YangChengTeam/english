package com.yc.junior.english.read.presenter;

import android.content.Context;

import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.read.contract.BookContract;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.engin.BookEngin;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by admin on 2017/8/7.
 */

public class BookPresenter extends BasePresenter<BookEngin, BookContract.View> implements BookContract.Presenter {

    public BookPresenter(Context context, BookContract.View view) {
        super(context, view);
        mEngine = new BookEngin(context);
    }

    @Override
    public void bookList(int currentPage, int pageCount, int type) {
        mView.showLoading();
        Subscription subscribe = mEngine.bookList(currentPage, pageCount, type).subscribe(new Subscriber<ArrayList<BookInfo>>() {
            @Override
            public void onCompleted() {
                UIUitls.postDelayed(500, new Runnable() {
                    @Override
                    public void run() {
                        mView.hide();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                mView.hide();
            }

            @Override
            public void onNext(final ArrayList<BookInfo> bookInfos) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showBookListData(bookInfos, false);
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }


    @Override
    public void addBook(BookInfo bookInfo) {
        Subscription subscribe = mEngine.addBook(bookInfo).subscribe(new Subscriber<ArrayList<BookInfo>>() {
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
                        mView.showBookListData(bookInfos, false);
                    }
                });
            }
        });
        mSubscriptions.add(subscribe);
    }

    @Override
    public void deleteBook(BookInfo bookInfo) {
        Subscription subscribe = mEngine.deleteBook(bookInfo).subscribe(new Subscriber<ArrayList<BookInfo>>() {
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
                        mView.showBookListData(bookInfos, true);
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
