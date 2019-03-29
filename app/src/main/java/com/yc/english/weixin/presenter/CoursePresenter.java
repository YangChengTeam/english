package com.yc.english.weixin.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.composition.model.bean.ReadNumInfo;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.weixin.contract.CourseContract;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.model.domain.CourseInfoWrapper;
import com.yc.english.weixin.model.engin.WeixinEngin;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CoursePresenter extends BasePresenter<WeixinEngin, CourseContract.View> implements CourseContract.Presenter {
    public static final String NEWSINFO = "newsListInfo";
    private boolean cached;

    public CoursePresenter(Context context, CourseContract.View iView) {
        super(context, iView);
        mEngine = new WeixinEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getWeiXinList(final String type_id, final int page,
                              int page_size) {
        if (page == 1) {
            mView.showLoading();

//            SimpleCacheUtils.readCache(mContext, NEWSINFO + type_id, new SimpleCacheUtils.CacheRunnable() {
//                @Override
//                public void run() {
//                    final List<CourseInfo> courseInfos = JSON.parseObject(getJson(), new TypeReference<List<CourseInfo>>() {
//                    }.getType());
//                    cached = true;
//                    UIUitls.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            showNewsListInfo(courseInfos, type_id, page, false);
//                        }
//                    });
//                }
//            });
        }


        Subscription subscription = mEngine.getWeixinList(type_id, page, page_size).subscribe(new Subscriber<ResultInfo<CourseInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (page == 1 && !cached) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<CourseInfoWrapper> courseInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(courseInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (page == 1 && !cached) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (page == 1 && !cached) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (courseInfoResultInfo.data != null) {
                            showNewsListInfo(courseInfoResultInfo.data.getList(), type_id, page, true);
                        } else {
                            if (cached) {
                                return;
                            }
                            if (page == 1) {
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

    private void showNewsListInfo(final List<CourseInfo> courseInfos, final String type_id, final int page, boolean isCache) {
        if (courseInfos != null && courseInfos.size() > 0) {
            if (isCache) {
                SimpleCacheUtils.writeCache(mContext, NEWSINFO + type_id, JSON.toJSONString(courseInfos));
            }
            mView.showWeixinList(courseInfos);
            if (page == 1) {
                mView.hide();
            }
        } else {
            if (page == 1) {
                mView.showNoData();
            }
            mView.end();
        }
    }

    public void statisticsNewsCount(String news_id) {
        Subscription subscription = EngineUtils.statisticsNewsCount(mContext, news_id).subscribe(new Subscriber<ResultInfo<ReadNumInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<ReadNumInfo> readNumInfoResultInfo) {

            }
        });
        mSubscriptions.add(subscription);
    }
}
