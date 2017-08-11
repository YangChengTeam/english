package com.yc.english.read.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.ReadWordContract;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.model.engin.WordEngin;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class ReadWordPresenter extends BasePresenter<WordEngin, ReadWordContract.View> implements ReadWordContract.Presenter {

    public ReadWordPresenter(Context context, ReadWordContract.View view) {
        super(context, view);
        mEngin = new WordEngin(context);
    }

    @Override
    public void getWordListByUnitId(int currentPage, int pageCount, String unitId) {
        Subscription subscribe = mEngin.getWordListByUnitId(currentPage, pageCount, unitId).subscribe(new Subscriber<List<WordInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<WordInfo> wordInfos) {
                if (wordInfos != null) {
                    mView.showWordListData(wordInfos);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
