package com.yc.english.speak.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.speak.contract.SpeakEnglishContract;
import com.yc.english.speak.model.bean.EnglishInfoWrapper;
import com.yc.english.speak.model.bean.SpeakAndReadInfoWrapper;
import com.yc.english.speak.model.engine.SpeakEnglishListEngine;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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


    @Override
    public void getEnglishInfoList(String type) {
        mView.showLoading();
        Subscription subscription = mEngin.getEnglishInfoList(type).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<EnglishInfoWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(EnglishInfoWrapper englishInfoWrapper) {
                if (englishInfoWrapper.getCode() == HttpConfig.STATUS_OK && englishInfoWrapper.getData() != null) {
                    mView.hideStateView();
//                    mView.showEnglishInfoList(englishInfoWrapper.getData().getList());
                } else {
                    mView.showNoData();
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getReadAndSpeakList(String type_id, final boolean isMore, final int page, final boolean isFitst) {
        if (page == 1 && isFitst) {
            mView.showLoading();
        }
        Subscription subscription = mEngin.getReadAndSpeakList(type_id, page + "").subscribe(new Subscriber<ResultInfo<SpeakAndReadInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1 && isFitst) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<SpeakAndReadInfoWrapper> englishInfoWrapper) {


                ResultInfoHelper.handleResultInfo(englishInfoWrapper, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1 && isFitst) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1 && isFitst) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (englishInfoWrapper.code == HttpConfig.STATUS_OK && englishInfoWrapper.data != null) {
                            mView.hideStateView();
                            if (isMore) {
                                mView.shoReadAndSpeakMorList(englishInfoWrapper.data.getList(), page, isFitst);
                            } else {
                                mView.showReadAndSpeakList(englishInfoWrapper.data.getSortList());
                            }
                        } else {
                            mView.showNoData();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
