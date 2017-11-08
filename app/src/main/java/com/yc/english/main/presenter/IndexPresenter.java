package com.yc.english.main.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.IndexEngin;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.pay.PayWayInfoHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexPresenter extends BasePresenter<IndexEngin, IndexContract.View> implements IndexContract.Presenter {
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
        Subscription subscription = mEngin.getIndexInfo().subscribe(new Subscriber<ResultInfo<IndexInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<IndexInfo> resultInfo) {
                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.hideStateView();
                        if (resultInfo.data.getSlideInfo() != null) {
                            List<String> images = new ArrayList<String>();
                            slideInfos = resultInfo.data.getSlideInfo();
                            for (SlideInfo slideInfo : resultInfo.data.getSlideInfo()) {
                                images.add(slideInfo.getImg());
                            }
                            mView.showBanner(images);
                            mView.showInfo(resultInfo.data);
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
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

