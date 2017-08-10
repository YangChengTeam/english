package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.WordUnitContract;
import com.yc.english.read.model.domain.BookInfoWarpper;
import com.yc.english.read.model.domain.WordUnitInfoList;
import com.yc.english.read.model.engin.WordEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class WordUnitPresenter extends BasePresenter<WordEngin, WordUnitContract.View> implements WordUnitContract.Presenter {

    public WordUnitPresenter(Context context, WordUnitContract.View view) {
        super(context, view);
        mEngin = new WordEngin(context);
    }

    @Override
    public void getBookInfoById(final String bookId) {
        Subscription subscribe = mEngin.getBookInfoId(bookId).subscribe(new Subscriber<ResultInfo<BookInfoWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<BookInfoWarpper> infoWarpper) {
                if (infoWarpper != null) {
                    mView.showBookInfo(infoWarpper.data.info);
                    mEngin.getWordUnitByBookId(0, 0, bookId).subscribe(new Subscriber<ResultInfo<WordUnitInfoList>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(final ResultInfo<WordUnitInfoList> resultInfo) {
                            if (resultInfo != null) {
                                mView.showWordUnitListData(resultInfo.data);
                            }
                        }
                    });

                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void getWordUnitByBookId(int currentPage, int pageCount, String bookId) {
        Subscription subscribe = mEngin.getWordUnitByBookId(currentPage, pageCount, bookId).subscribe(new Subscriber<ResultInfo<WordUnitInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<WordUnitInfoList> resultInfo) {
                if (resultInfo != null) {
                    mView.showWordUnitListData(resultInfo.data);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
