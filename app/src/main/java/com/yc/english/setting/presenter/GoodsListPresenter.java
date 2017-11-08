package com.yc.english.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.contract.GoodsListContract;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.setting.model.bean.PayWayInfo;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/11/6 15:55.
 */

public class GoodsListPresenter extends BasePresenter<BaseEngin, GoodsListContract.View> implements GoodsListContract.Presenter {
    public GoodsListPresenter(Context context, GoodsListContract.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getGoodsList(1, 1);
        getPayWayList();
    }

    public void getGoodsList(int goods_type_id, int page) {
        mView.showLoading();
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
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.hideStateView();
                        mView.showGoodVipList(goodInfoWrapperResultInfo.data.getList());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    public void getPayWayList() {
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
                        mView.showPayWayList(payWayInfoResultInfo.data);
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    public void createOrder(final OrderParams orderParams) {
        mView.showLoadingDialog("创建订单中，请稍候...");
        Subscription subscription = EngineUtils.createOrder(mContext, orderParams.getTitle(), orderParams.getMoney(), orderParams.getMoney(), orderParams.getPayWayName(), orderParams.getGoodsList()).subscribe(new Subscriber<ResultInfo<OrderInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> orderInfoResultInfo) {
                handleResultInfo(orderInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.dismissLoadingDialog();
                        mView.showOrderInfo(orderInfoResultInfo.data, orderParams.getMoney(), orderParams.getTitle());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

}
