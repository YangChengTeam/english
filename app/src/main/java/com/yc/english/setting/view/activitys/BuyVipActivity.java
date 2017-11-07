package com.yc.english.setting.view.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.contract.GoodsListContract;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.presenter.GoodsListPresenter;
import com.yc.english.setting.view.adapter.GoodVipInfoAdapter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by admin on 2017/10/31.
 */

public class BuyVipActivity extends FullScreenActivity<GoodsListPresenter> implements GoodsListContract.View {


    @BindView(R.id.layout_alipay)
    RelativeLayout mAlipayLayout;

    @BindView(R.id.layout_weixin)
    RelativeLayout mWeiXinLayout;


    @BindView(R.id.ck_alipay)
    CheckBox mAlipayCkBox;

    @BindView(R.id.ck_weixin)
    CheckBox mWeiXinBox;

    @BindView(R.id.layout_confirm_pay)
    LinearLayout mLayoutConfirmPay;

    @BindView(R.id.recycler_vip)
    RecyclerView mRecyclerVip;

    @BindView(R.id.stateView)
    StateView mStateView;
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;

    private GoodVipInfoAdapter goodVipInfoAdapter;


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

        RxView.clicks(mAlipayLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mWeiXinBox.setChecked(false);
                mAlipayCkBox.setChecked(true);
            }
        });
        RxView.clicks(mWeiXinLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mAlipayCkBox.setChecked(false);
                mWeiXinBox.setChecked(true);
            }
        });

        goodVipInfoAdapter.setOnVipClickListener(new GoodVipInfoAdapter.onVipClickListener() {
            @Override
            public void onVipClick(GoodInfo info) {
                LogUtils.e(info.getM_price());
            }
        });


        RxView.clicks(mLayoutConfirmPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {



                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setMoney(1.0f);
                orderInfo.setOrder_sn(System.currentTimeMillis() + "");
                orderInfo.setName("vip课程购买");

                new IAliPay1Impl(BuyVipActivity.this).pay(orderInfo, new IPayCallback() {
                    @Override
                    public void onSuccess(OrderInfo orderInfo) {

                    }

                    @Override
                    public void onFailure(OrderInfo orderInfo) {

                    }
                });
            }
        });

    }

    @Override
    public void showGoodVipList(List<GoodInfo> list) {

        goodVipInfoAdapter.setNewData(list);
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
