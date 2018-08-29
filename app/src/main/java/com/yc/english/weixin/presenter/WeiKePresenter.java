package com.yc.english.weixin.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.UIUitls;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.weixin.contract.WeiKeContract;
import com.yc.english.weixin.model.domain.WeiKeCategory;
import com.yc.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.english.weixin.model.domain.WeiKeInfoWrapper;
import com.yc.english.weixin.model.engin.WeiKeEngin;
import com.zhihu.matisse.internal.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKePresenter extends BasePresenter<WeiKeEngin, WeiKeContract.View> implements WeiKeContract.Presenter {

    private final String WEIKE_INFO = "weike_info";
    private final String SPOKEN_INFO = "spoken_info";

    public WeiKePresenter(Context context, WeiKeContract.View iView) {
        super(context, iView);
        mEngin = new WeiKeEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getWeikeCategoryList(String type, final String page) {
        if (page.equals("1")) {
            mView.showLoading();
        }
        String key = "";
        if (TextUtils.equals("7", type)) {
            key = SPOKEN_INFO;
        } else if (TextUtils.equals("8", type)) {
            key = WEIKE_INFO;
        }


        SimpleCacheUtils.readCache(mContext, key, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final WeiKeCategoryWrapper weiKeCategoryWrapper = JSON.parseObject(this.getJson(), WeiKeCategoryWrapper.class);
                cached = true;
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideStateView();
                        mView.showWeikeCategoryList(weiKeCategoryWrapper);

                    }
                });

            }
        });

        if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_NO) {
            return;
        }
        final String finalKey = key;
        Subscription subscription = mEngin.getWeikeCategoryList(type, page).subscribe(new Subscriber<ResultInfo<WeiKeCategoryWrapper>>() {
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
                        if (weikeCategoryWrapper.data != null) {
                            showWeikeCategoryList(weikeCategoryWrapper.data, finalKey, true);

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

    private void showWeikeCategoryList(WeiKeCategoryWrapper categoryWrapper, String finalKey, boolean isCached) {
        if (isCached) {
            SimpleCacheUtils.writeCache(mContext, finalKey, JSON.toJSONString(categoryWrapper));
        }
        mView.showWeikeCategoryList(categoryWrapper);

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
                            mView.showNoNet();
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
