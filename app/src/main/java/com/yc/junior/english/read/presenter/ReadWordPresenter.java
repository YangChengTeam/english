package com.yc.junior.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.read.contract.ReadWordContract;
import com.yc.junior.english.read.model.domain.WordInfoList;
import com.yc.junior.english.read.model.engin.WordEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;


/**
 * Created by admin on 2017/8/7.
 */

public class ReadWordPresenter extends BasePresenter<WordEngin, ReadWordContract.View> implements ReadWordContract.Presenter {

    public ReadWordPresenter(Context context, ReadWordContract.View view) {
        super(context, view);
        mEngine = new WordEngin(context);
    }

    @Override
    public void getWordListByUnitId(int currentPage, int pageCount, String unitId) {
        mView.showLoading();
        Subscription subscribe = mEngine.getWordListByUnitId(currentPage, pageCount, unitId).subscribe(new Subscriber<ResultInfo<WordInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<WordInfoList> resultInfo) {


                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
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
                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showWordListData(resultInfo.data.list);
                            mView.hide();
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
