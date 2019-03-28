package com.yc.english.composition.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.model.Config;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.composition.contract.EssayContract;
import com.yc.english.composition.model.bean.CompositionInfo;
import com.yc.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.english.composition.model.bean.ReadNumInfo;
import com.yc.english.composition.model.bean.VersionInfo;
import com.yc.english.composition.model.engine.EssayEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.soundmark.base.constant.SpConstant;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.CommonInfoHelper;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by wanglin  on 2019/3/22 18:20.
 */
public class EssayPresenter extends BasePresenter<EssayEngine, EssayContract.View> implements EssayContract.Presenter {


    public EssayPresenter(Context context, EssayContract.View view) {
        super(context, view);
        mEngine = new EssayEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void getCompositionInfos(String attrid, final int page, int pageSize, boolean isRefresh) {
        if (page == 1 && !isRefresh)
            mView.showLoading();
        Subscription subscription = mEngine.getCompositionInfos(attrid, page, pageSize).subscribe(new Subscriber<ResultInfo<CompositionInfoWrapper>>() {
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
                    if (compositionInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && compositionInfoWrapperResultInfo.data != null && compositionInfoWrapperResultInfo.data.getList() != null) {
                        mView.hide();
                        mView.showCompositionInfos(compositionInfoWrapperResultInfo.data.getList());
                        if (compositionInfoWrapperResultInfo.data.getList().size() == 0) {
                            if (page == 1) {
                                mView.showNoData();
                            }
                        }
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


    @Override
    public void getCompositionIndexInfo() {
        SimpleCacheUtils.readCache(mContext, Constant.COMPOSITION_INDEX_INFO, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                String json = getJson();
                if (!TextUtils.isEmpty(json)) {
                    final CompositionInfoWrapper compositionInfoWrapper = JSON.parseObject(this.getJson(), CompositionInfoWrapper.class);
                    UIUitls.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.showCompositionIndexInfo(compositionInfoWrapper);
                        }
                    });
                }
            }
        });

        Subscription subscription = mEngine.getCompositionIndexInfo().subscribe(new Subscriber<ResultInfo<CompositionInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<CompositionInfoWrapper> compositionInfoWrapperResultInfo) {
                if (compositionInfoWrapperResultInfo != null && compositionInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && compositionInfoWrapperResultInfo.data != null) {
                    mView.showCompositionIndexInfo(compositionInfoWrapperResultInfo.data);
                    VersionInfo versionInfo = compositionInfoWrapperResultInfo.data.getOption();
                    CommonInfoHelper.setO(mContext, versionInfo, SpConstant.INDEX_VERSION);
                    showIndexInfo(compositionInfoWrapperResultInfo.data.getSlideList());
                    SimpleCacheUtils.writeCache(mContext, Constant.COMPOSITION_INDEX_INFO, JSON.toJSONString(compositionInfoWrapperResultInfo.data));

                }
            }
        });
        mSubscriptions.add(subscription);
    }

    private void showIndexInfo(List<SlideInfo> slideInfos) {
        if (slideInfos != null) {

            List<String> images = new ArrayList<>();
            mSlideInfos = slideInfos;
            for (SlideInfo slideInfo : slideInfos) {
                images.add(slideInfo.getImg());
            }
            mView.showBanner(images);
        }
    }


    private List<SlideInfo> mSlideInfos;


    public SlideInfo getSlideInfo(int position) {
        if (mSlideInfos != null && mSlideInfos.size() > position) {
            return mSlideInfos.get(position);
        }
        return null;
    }

    public void statisticsReadCount(String id) {
        Subscription subscription = EngineUtils.statisticsReadCount(mContext, id).subscribe(new Subscriber<ResultInfo<ReadNumInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<ReadNumInfo> stringResultInfo) {

            }
        });
        mSubscriptions.add(subscription);
    }

}
