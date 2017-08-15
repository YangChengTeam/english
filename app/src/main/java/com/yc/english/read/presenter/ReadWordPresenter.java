package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.ReadWordContract;
import com.yc.english.read.model.domain.WordInfoList;
import com.yc.english.read.model.engin.WordEngin;

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
        Subscription subscribe = mEngin.getWordListByUnitId(currentPage, pageCount, unitId).subscribe(new Subscriber<ResultInfo<WordInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<WordInfoList> resultInfo) {
                if (resultInfo != null && resultInfo.data != null) {
                    mView.showWordListData(resultInfo.data.list);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
