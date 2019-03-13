package com.yc.english.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.contract.MyContract;
import com.yc.english.setting.model.bean.MyOrderInfo;
import com.yc.english.setting.model.bean.ScoreInfo;
import com.yc.english.setting.model.engin.MyEngin;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class MyPresenter extends BasePresenter<MyEngin, MyContract.View> implements MyContract.Presenter {

    public MyPresenter(Context context, MyContract.View iView) {
        super(context, iView);
        mEngine = new MyEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getUserInfo();
    }


    @Override
    public void getUserInfo() {
        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showUserInfo(userInfo);

            }

            @Override
            public void showNoLogin() {
                mView.showNoLogin(true);
            }
        });
    }

    @Override
    public void getMyOrderInfoList(final int currentPage, int limit) {
        if (currentPage == 1 && mFirstLoad) {
            mView.showLoading();
        }
        Subscription subscription = mEngine.getMyOrderInfo(currentPage, limit).subscribe(new Subscriber<ResultInfo<List<MyOrderInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (currentPage == 1 && mFirstLoad) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<List<MyOrderInfo>> resultInfo) {
                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null) {
                            if (currentPage == 1 && !mFirstLoad) {
                                mView.hide();
                            }

                            if (resultInfo.data != null && resultInfo.data.size() > 0) {
                                mView.showMyOrderInfoList(resultInfo.data);
                            } else {
                                if (currentPage == 1 && !mFirstLoad) {
                                    mView.showNoData();
                                } else {
                                    mView.showMyOrderInfoList(null);
                                }
                            }
                        } else {
                            if (currentPage == 1 && !mFirstLoad) {
                                mView.showNoData();
                            }
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getAbilityScore(String uid) {
        Subscription subscription = mEngine.getAbilityScore(uid).subscribe(new Subscriber<ResultInfo<ScoreInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ScoreInfo> scoreInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(scoreInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (scoreInfoResultInfo != null && scoreInfoResultInfo.data != null)
                            mView.showScoreResult(scoreInfoResultInfo.data);
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }
}
