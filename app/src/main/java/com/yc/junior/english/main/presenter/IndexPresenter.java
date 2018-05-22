package com.yc.junior.english.main.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.UIUitls;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.EnglishApp;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.base.utils.SimpleCacheUtils;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.contract.IndexContract;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.IndexInfo;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.main.model.engin.IndexEngin;
import com.yc.junior.english.pay.PayWayInfo;
import com.yc.junior.english.pay.PayWayInfoHelper;
import com.yc.junior.english.setting.model.bean.GoodInfoWrapper;
import com.yc.junior.english.setting.model.bean.ShareStateInfo;
import com.yc.junior.english.vip.utils.VipInfoHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexPresenter extends BasePresenter<IndexEngin, IndexContract.View> implements IndexContract.Presenter {
    public static final String INDEX_INFO = "getIndexInfo";

    public IndexPresenter(Context context, IndexContract.View view) {
        super(context, view);
        mEngin = new IndexEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

        getIndexInfo();
        getPayWayList();
        getGoodsList(1);
        getOpenShareVip();
    }


    @Override
    public void getIndexInfo() {
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
                        mView.hideStateView();
                        showIndexInfo(indexInfo, false);
                    }
                });
            }
        });

        Subscription subscription = mEngin.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfo>>() {
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
                        mView.hideStateView();
                        showIndexInfo(resultInfo.data, true);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    private void showIndexInfo(IndexInfo indexInfo, boolean isCached) {
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
            mView.showInfo(indexInfo);
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


    /**
     * 分享是否开启体验VIP
     */
    private void getOpenShareVip() {

        Subscription subscription = EngineUtils.getShareVipState(mContext).subscribe(new Subscriber<ResultInfo<ShareStateInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<ShareStateInfo> shareResult) {
                ResultInfoHelper.handleResultInfo(shareResult, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (shareResult.data != null) {
                            if(shareResult.data.getStatus() == 1){
                                EnglishApp.isOpenShareVip = true;
                                EnglishApp.trialDays = shareResult.data.getDays();
                            }
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

}

