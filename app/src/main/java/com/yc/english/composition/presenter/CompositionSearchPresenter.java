package com.yc.english.composition.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.composition.contract.CompositionSearchContract;
import com.yc.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.english.composition.model.engine.CompositionSearchEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2019/3/26 17:22.
 */
public class CompositionSearchPresenter extends BasePresenter<CompositionSearchEngine, CompositionSearchContract.View> implements CompositionSearchContract.Presenter {
    public CompositionSearchPresenter(Context context, CompositionSearchContract.View view) {
        super(context, view);
        mEngine = new CompositionSearchEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    @Override
    public void searchCompositionInfos(String title, String grade_id, String topic, String ticai, String type, final int page, int pagesize) {
        if (page == 1)
            mView.showLoading();
        Subscription subscription = mEngine.searchCompositionInfos(title, grade_id, topic, ticai, type, page, pagesize).subscribe(new Subscriber<ResultInfo<CompositionInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1)
                    mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<CompositionInfoWrapper> compositionInfoWrapperResultInfo) {
                if (compositionInfoWrapperResultInfo != null) {
                    if (compositionInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && compositionInfoWrapperResultInfo.data != null &&
                            compositionInfoWrapperResultInfo.data.getList() != null && compositionInfoWrapperResultInfo.data.getList().size() > 0) {
                        mView.hide();
                        mView.showSearchResult(compositionInfoWrapperResultInfo.data.getList());
                    } else {
                        if (page == 1)
                            mView.showNoData();
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
