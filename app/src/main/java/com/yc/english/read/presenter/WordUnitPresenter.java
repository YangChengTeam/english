package com.yc.english.read.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.WordUnitContract;
import com.yc.english.read.model.domain.WordUnitInfo;
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
    public void bookUnitInfo(int currentPage, int pageCount) {
        Subscription subscribe = mEngin.wordUnitInfo(currentPage, pageCount).subscribe(new Subscriber<WordUnitInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final WordUnitInfo wordUnitInfo) {
                if (wordUnitInfo != null) {
                    mView.showWordUnitListData(wordUnitInfo);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
