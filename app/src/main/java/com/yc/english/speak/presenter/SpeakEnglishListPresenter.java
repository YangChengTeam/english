package com.yc.english.speak.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.speak.contract.SpeakEnglishContract;
import com.yc.english.speak.model.bean.SpeakAndReadInfoWrapper;
import com.yc.english.speak.model.bean.SpeakEnglishWarpper;
import com.yc.english.speak.model.engine.SpeakEnglishListEngine;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/10/13 08:59.
 */

public class SpeakEnglishListPresenter extends BasePresenter<SpeakEnglishListEngine, SpeakEnglishContract.View> implements SpeakEnglishContract.Presenter {
    public SpeakEnglishListPresenter(Context context, SpeakEnglishContract.View view) {
        super(context, view);
        mEngin = new SpeakEnglishListEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }


    public void getReadAndSpeakList(String type_id, String cnt, final int page, final boolean isFirst) {
        if (page == 1 && isFirst) {
            mView.showLoading();
        }
        Subscription subscription = mEngin.getReadAndSpeakList(type_id, page + "", cnt).subscribe(new Subscriber<ResultInfo<SpeakAndReadInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1 && isFirst) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<SpeakAndReadInfoWrapper> englishInfoWrapper) {


                ResultInfoHelper.handleResultInfo(englishInfoWrapper, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1 && isFirst) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1 && isFirst) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (englishInfoWrapper.code == HttpConfig.STATUS_OK && englishInfoWrapper.data != null) {
                            mView.hideStateView();
                            mView.shoReadAndSpeakMorList(englishInfoWrapper.data.getList(), page, isFirst);
                        } else {
                            mView.showNoData();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getListenEnglishDetail(String id) {
        Subscription subscribe = mEngin.getListenReadDetail(mContext, id).subscribe(new Subscriber<ResultInfo<SpeakEnglishWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<SpeakEnglishWarpper> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null && resultInfo.data.info != null) {
                            mView.showSpeakEnglishDetail(resultInfo.data.info);
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscribe);
    }
}
