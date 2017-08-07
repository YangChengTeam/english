package com.yc.english.read.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.BookUnitContract;
import com.yc.english.read.model.domain.BookUnitInfo;
import com.yc.english.read.model.engin.BookEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class BookUnitPresenter extends BasePresenter<BookEngin, BookUnitContract.View> implements BookUnitContract.Presenter {

    public BookUnitPresenter(Context context, BookUnitContract.View view) {
        super(context, view);
        mEngin = new BookEngin(context);
    }

    @Override
    public void bookUnitInfo(int currentPage, int pageCount) {
        Subscription subscribe = mEngin.bookUnitInfo(currentPage, pageCount).subscribe(new Subscriber<BookUnitInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final BookUnitInfo bookUnitInfo) {
                if (bookUnitInfo != null) {
                    mView.showBookUnitListData(bookUnitInfo);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
