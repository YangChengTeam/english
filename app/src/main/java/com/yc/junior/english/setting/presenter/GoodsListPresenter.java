package com.yc.junior.english.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.news.model.domain.OrderParams;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.setting.contract.GoodsListContract;
import com.yc.junior.english.setting.model.bean.GoodInfoWrapper;

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
                        mView.hide();
//                        mView.showGoodVipList(goodInfoWrapperResultInfo.data.getList());
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
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<OrderInfo> orderInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(orderInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.dismissDialog();
                        TipsHelper.tips(mContext,message);
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.dismissDialog();
                        TipsHelper.tips(mContext,message);
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.dismissDialog();
                        mView.showOrderInfo(orderInfoResultInfo.data, orderParams.getMoney(), orderParams.getTitle());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

}
