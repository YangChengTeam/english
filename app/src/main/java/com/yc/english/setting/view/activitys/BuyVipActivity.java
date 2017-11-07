package com.yc.english.setting.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.pay.PayConfig;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.contract.GoodsListContract;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.model.bean.PayWayInfo;
import com.yc.english.setting.presenter.GoodsListPresenter;
import com.yc.english.setting.view.Listener.onItemClickListener;
import com.yc.english.setting.view.adapter.GoodPayWayInfoAdapter;
import com.yc.english.setting.view.adapter.GoodVipInfoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/10/31.
 */

public class BuyVipActivity extends FullScreenActivity<GoodsListPresenter> implements GoodsListContract.View {


    @BindView(R.id.layout_confirm_pay)
    LinearLayout mLayoutConfirmPay;

    @BindView(R.id.recycler_vip)
    RecyclerView mRecyclerVip;

    @BindView(R.id.stateView)
    StateView mStateView;
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;

    @BindView(R.id.recycler_pay_way)
    RecyclerView mRecyclerPayWay;

    private GoodVipInfoAdapter goodVipInfoAdapter;
    private String pay_way_name = PayConfig.ali_pay;
    private GoodPayWayInfoAdapter goodPayWayInfoAdapter;

    private GoodInfo goodInfo;


    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    public void init() {
        mPresenter = new GoodsListPresenter(this, this);
        mToolbar.setTitle("VIP会员");
        mToolbar.showNavigationIcon();

        mRecyclerVip.setLayoutManager(new LinearLayoutManager(this));
        goodVipInfoAdapter = new GoodVipInfoAdapter(null);
        mRecyclerVip.setAdapter(goodVipInfoAdapter);

        mRecyclerPayWay.setLayoutManager(new LinearLayoutManager(this));
        goodPayWayInfoAdapter = new GoodPayWayInfoAdapter(null);
        mRecyclerPayWay.setAdapter(goodPayWayInfoAdapter);

        goodVipInfoAdapter.setOnItemClickListener(new onItemClickListener<GoodInfo>() {
            @Override
            public void onItemClick(GoodInfo info) {
                goodInfo = info;
            }
        });
        goodPayWayInfoAdapter.setOnItemClickListener(new onItemClickListener<PayWayInfo>() {
            @Override
            public void onItemClick(PayWayInfo payWayInfo) {
                pay_way_name = payWayInfo.getPay_way_name();
            }
        });


        RxView.clicks(mLayoutConfirmPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                List<OrderInfo> list = new ArrayList<>();
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGood_id(Integer.parseInt(goodInfo.getId()));
                orderInfo.setNum(1);
                list.add(orderInfo);
                mPresenter.createOrder(goodInfo.getName(), goodInfo.getM_price(), goodInfo.getM_price(), pay_way_name, list);

//                OrderInfo orderInfo = new OrderInfo();
//                orderInfo.setMoney(1.0f);
//                orderInfo.setOrder_sn(System.currentTimeMillis() + "");
//                orderInfo.setName("vip课程购买");
//
//                new IAliPay1Impl(BuyVipActivity.this).pay(orderInfo, new IPayCallback() {
//                    @Override
//                    public void onSuccess(OrderInfo orderInfo) {
//
//                    }
//
//                    @Override
//                    public void onFailure(OrderInfo orderInfo) {
//
//                    }
//                });
            }
        });

    }

    @Override
    public void showGoodVipList(List<GoodInfo> list) {

        goodInfo = list.get(0);
        goodVipInfoAdapter.setNewData(list);
    }

    @Override
    public void showPayWayList(List<PayWayInfo> data) {
        goodPayWayInfoAdapter.setNewData(data);
    }


    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mRlContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mRlContainer);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mRlContainer);
    }
}
