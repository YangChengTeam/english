package com.yc.english.main.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.subutil.util.ThreadPoolUtils;
import com.blankj.utilcode.util.SPUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.helper.RxUtils;
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

import org.greenrobot.greendao.annotation.Index;

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


}

