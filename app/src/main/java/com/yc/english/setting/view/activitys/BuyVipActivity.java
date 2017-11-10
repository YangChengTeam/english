package com.yc.english.setting.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.PayConfig;
import com.yc.english.pay.PayWayInfoHelper;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.contract.GoodsListContract;
import com.yc.english.setting.model.bean.Config;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.setting.presenter.GoodsListPresenter;
import com.yc.english.setting.view.Listener.onItemClickListener;
import com.yc.english.setting.view.adapter.GoodPayWayInfoAdapter;
import com.yc.english.setting.view.adapter.GoodVipInfoAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    @BindView(R.id.tv_pay_money)
    TextView mTvPayMoney;

    private GoodVipInfoAdapter goodVipInfoAdapter;
    private String pay_way_name = PayConfig.ali_pay;
    private GoodPayWayInfoAdapter goodPayWayInfoAdapter;

    private GoodInfo goodInfo;
    private IAliPay1Impl iAliPay;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    public void init() {
        mPresenter = new GoodsListPresenter(this, this);
        mToolbar.setTitle("VIP会员");
        mToolbar.showNavigationIcon();
        iAliPay = new IAliPay1Impl(this);
        mRecyclerVip.setLayoutManager(new LinearLayoutManager(this));
        goodVipInfoAdapter = new GoodVipInfoAdapter(null);
        mRecyclerVip.setAdapter(goodVipInfoAdapter);

        mRecyclerPayWay.setLayoutManager(new LinearLayoutManager(this));
        goodPayWayInfoAdapter = new GoodPayWayInfoAdapter(PayWayInfoHelper.getPayWayInfoList());
        mRecyclerPayWay.setAdapter(goodPayWayInfoAdapter);

        goodVipInfoAdapter.setOnItemClickListener(new onItemClickListener<GoodInfo>() {
            @Override
            public void onItemClick(GoodInfo info) {
                goodInfo = info;
                setPayPrice(goodInfo.getM_price());
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

                OrderParams orderParams = new OrderParams();
                orderParams.setTitle(goodInfo.getName());
                orderParams.setMoney(goodInfo.getM_price());
                orderParams.setPayWayName(pay_way_name);
                List<OrderGood> list = new ArrayList<>();
                OrderGood orderGood = new OrderGood();
                orderGood.setGood_id(goodInfo.getId());
                orderGood.setNum(1);

                list.add(orderGood);
                orderParams.setGoodsList(list);

                mPresenter.createOrder(orderParams);

            }
        });

    }

    @Override
    public void showGoodVipList(List<GoodInfo> list) {
        Collections.sort(list, new Comparator<GoodInfo>() {
            @Override
            public int compare(GoodInfo o1, GoodInfo o2) {
                return Integer.parseInt(o1.getUse_time_limit()) - Integer.parseInt(o2.getUse_time_limit());
            }
        });
        goodInfo = list.get(0);
        setPayPrice(goodInfo.getM_price());
        goodVipInfoAdapter.setNewData(list);
    }


    @Override
    public void showOrderInfo(OrderInfo orderInfo, String money, String name) {

        if (pay_way_name.equals(PayConfig.ali_pay)) {
            orderInfo.setMoney(Float.parseFloat(money));
            orderInfo.setName(name);
            iAliPay.pay(orderInfo, new IPayCallback() {
                @Override
                public void onSuccess(OrderInfo orderInfo) {
                    updateSuccessData();
                }

                @Override
                public void onFailure(OrderInfo orderInfo) {
                }
            });
        } else {
            //todo 微信支付
        }
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

    private void setPayPrice(String money) {
        String str = getString(R.string.confirm_pay);
        String result = String.format(str, Float.parseFloat(money));
        mTvPayMoney.setText(result);
    }

    private void updateSuccessData() {
        UserInfoHelper.getUserInfo().setIsVip(1);
        Date date = new Date();
        UserInfoHelper.getUserInfo().setVip_start_time(date.getTime());
        int use_time_Limit = Integer.parseInt(goodInfo.getUse_time_limit());
        long vip_end_time = date.getTime() + use_time_Limit * 30 * (Config.MS_IN_A_DAY);
        UserInfoHelper.getUserInfo().setVip_end_time(vip_end_time);
        finish();
    }

}
