package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.BookUnitContract;
import com.yc.english.read.model.domain.BookInfoWarpper;
import com.yc.english.read.model.domain.UnitInfoList;
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
    public void getBookInfoById(final String bookId) {
        mView.showLoading();
        Subscription subscribe = mEngin.getBookInfoId(bookId).subscribe(new Subscriber<ResultInfo<BookInfoWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<BookInfoWarpper> infoWarpper) {
                ResultInfoHelper.handleResultInfo(infoWarpper, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void reulstInfoOk() {

                        mEngin.bookUnitInfo(0, 0, bookId).subscribe(new Subscriber<ResultInfo<UnitInfoList>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showNoNet();
                            }

                            @Override
                            public void onNext(final ResultInfo<UnitInfoList> resultInfo) {
                                if (resultInfo != null) {
                                    mView.showBookInfo(infoWarpper.data.info);
                                    mView.showBookUnitListData(resultInfo.data);
                                    mView.hideStateView();
                                } else {
                                    mView.showNoData();
                                }
                            }
                        });
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void bookUnitInfo(int currentPage, int pageCount, String bookId) {
        Subscription subscribe = mEngin.bookUnitInfo(currentPage, pageCount, bookId).subscribe(new Subscriber<ResultInfo<UnitInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<UnitInfoList> resultInfo) {
                if (resultInfo != null) {
                    mView.showBookUnitListData(resultInfo.data);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
