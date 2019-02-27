package com.yc.english.main.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.IndexEngin;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.pay.PayWayInfoHelper;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.vip.utils.VipInfoHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexPresenter extends BasePresenter<IndexEngin, IndexContract.View> implements IndexContract.Presenter {
    public static final String INDEX_INFO = "getIndexInfo";

    public IndexPresenter(Context context, IndexContract.View view) {
        super(context, view);
        mEngine = new IndexEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

        getIndexInfo(false);
        getPayWayList();
        getGoodsList(1);

    }


    @Override
    public void getIndexInfo(final boolean isFresh) {
        getAvatar();

        mView.showLoading();
        SimpleCacheUtils.readCache(mContext, INDEX_INFO, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final IndexInfo indexInfo = JSON.parseObject(this.getJson(), IndexInfo.class);
                cached = true;
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hide();
                        showIndexInfo(indexInfo, false, isFresh);
                    }
                });
            }
        });

        Subscription subscription = mEngine.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!cached) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<IndexInfo> resultInfo) {
                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (!cached) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (!cached) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.hide();
                        showIndexInfo(resultInfo.data, true, isFresh);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    private void showIndexInfo(IndexInfo indexInfo, boolean isCached, boolean isFresh) {
        if (indexInfo.getSlideInfo() != null) {
            if (isCached) {
                SimpleCacheUtils.writeCache(mContext, INDEX_INFO, JSON.toJSONString(indexInfo));
            }
            List<String> images = new ArrayList<String>();
            slideInfos = indexInfo.getSlideInfo();
            for (SlideInfo slideInfo : indexInfo.getSlideInfo()) {
                images.add(slideInfo.getImg());
            }
            mView.showBanner(images);
            mView.showInfo(indexInfo, isFresh);
        }
    }


    private List<SlideInfo> slideInfos;

    @Override
    public SlideInfo getSlideInfo(int position) {
        if (slideInfos != null && slideInfos.size() > position) {
            return slideInfos.get(position);
        }
        return null;
    }


    @Override
    public void getAvatar() {
        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showAvatar(userInfo);
            }

            @Override
            public void showNoLogin() {

            }
        });
    }

    private void getPayWayList() {
        Subscription subscription = EngineUtils.getPayWayList(mContext).subscribe(new Subscriber<ResultInfo<List<PayWayInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<List<PayWayInfo>> payWayInfoResultInfo) {
                handleResultInfo(payWayInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        PayWayInfoHelper.setPayWayInfoList(payWayInfoResultInfo.data);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    private void getGoodsList(int goods_type_id) {

        Subscription subscription = EngineUtils.getGoodsList(mContext, goods_type_id, 1).subscribe(new Subscriber<ResultInfo<GoodInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<GoodInfoWrapper> goodInfoWrapperResultInfo) {
                ResultInfoHelper.handleResultInfo(goodInfoWrapperResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (goodInfoWrapperResultInfo.data != null) {

                            VipInfoHelper.setGoodInfoWrapper(goodInfoWrapperResultInfo.data);
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }





}

