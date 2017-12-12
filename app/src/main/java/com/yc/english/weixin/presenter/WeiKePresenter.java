package com.yc.english.weixin.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.weixin.contract.WeiKeContract;
import com.yc.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.english.weixin.model.domain.WeiKeInfoWrapper;
import com.yc.english.weixin.model.engin.WeiKeEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKePresenter extends BasePresenter<WeiKeEngin, WeiKeContract.View> implements WeiKeContract.Presenter {
    public WeiKePresenter(Context context, WeiKeContract.View iView) {
        super(context, iView);
        mEngin = new WeiKeEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getWeikeCategoryList(final String page) {
        if (page.equals("1")) {
            mView.showLoading();
        }
        Subscription subscription = mEngin.getWeikeCategoryList(null, page).subscribe(new Subscriber<ResultInfo<WeiKeCategoryWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page.equals("1")) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<WeiKeCategoryWrapper> weikeCategoryWrapper) {
                ResultInfoHelper.handleResultInfo(weikeCategoryWrapper, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page.equals("1")) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page.equals("1")) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (weikeCategoryWrapper.data != null && weikeCategoryWrapper.data.getList() != null &&
                                weikeCategoryWrapper.data.getList().size() > 0) {
                            mView.showWeikeCategoryList(weikeCategoryWrapper.data.getList());
                            if (page.equals("1")) {
                                mView.hideStateView();
                            }
                        } else {
                            if (page.equals("1")) {
                                mView.showNoData();
                            }
                            mView.end();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getWeiKeInfoList(String pid, final String page) {
        if (page.equals("1")) {
            mView.showLoading();
        }
        Subscription subscription = mEngin.getWeiKeInfoList(pid, page).subscribe(new Subscriber<ResultInfo<WeiKeInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page.equals("1")) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<WeiKeInfoWrapper> weiKeInfoWrapper) {
                ResultInfoHelper.handleResultInfo(weiKeInfoWrapper, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page.equals("1")) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page.equals("1")) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (weiKeInfoWrapper.data != null && weiKeInfoWrapper.data.getList() != null &&
                                weiKeInfoWrapper.data.getList().size() > 0) {
                            mView.showWeiKeInfoList(weiKeInfoWrapper.data.getList());
                            if (page.equals("1")) {
                                mView.hideStateView();
                            }
                        } else {
                            if (page.equals("1")) {
                                mView.showNoData();
                            }
                            mView.end();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
