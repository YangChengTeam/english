package com.yc.english.news.view.activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.R;
import com.yc.english.base.helper.ShoppingHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.adapter.OrderItemAdapter;
import com.yc.english.news.contract.OrderContract;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.news.presenter.OrderPresenter;
import com.yc.english.news.utils.OrderConstant;
import com.yc.english.news.view.widget.SpaceItemDecoration;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/11/2.
 */

public class ConfirmOrderActivity extends FullScreenActivity<OrderPresenter> implements OrderContract.View {

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.rv_order_list)
    RecyclerView mOrderRecyclerView;

    @BindView(R.id.tv_total_price)
    TextView mTotalPrice;

    @BindView(R.id.layout_pay_order)
    LinearLayout mPayOrderLayout;

    OrderItemAdapter mOrderItemAdapter;

    OrderParams orderParams;

    private float totalPrice;

    private ArrayList<CourseInfo> orderList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void init() {
        mToolbar.setTitle("确认订单");
        mToolbar.showNavigationIcon();

        Intent intent = getIntent();
        orderList = intent.getParcelableArrayListExtra("goods_list");

        mPresenter = new OrderPresenter(this, this);
        totalPrice = intent.getFloatExtra("total_price", 0);
        mTotalPrice.setText(totalPrice + "");
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        mOrderItemAdapter = new OrderItemAdapter(orderList);
        mOrderItemAdapter.setFooterView(getFootView());
        mOrderRecyclerView.setAdapter(mOrderItemAdapter);

        List<OrderGood> goods = new ArrayList<>();
        if (orderList != null && orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                OrderGood orderGood = new OrderGood();
                orderGood.setGood_id(orderList.get(i).getGoodId());
                orderGood.setNum(1);
                goods.add(orderGood);
            }
        }

        orderParams = new OrderParams();
        orderParams.setPriceTotal(intent.getFloatExtra("total_price", 0) + "");
        orderParams.setPayWayName("alipay");
        orderParams.setGoodsList(goods);

        RxView.clicks(mPayOrderLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.createOrder(orderParams);
            }
        });
    }

    public View getFootView() {
        View footView = getLayoutInflater().inflate(R.layout.activity_order_foot, (ViewGroup) mOrderRecyclerView.getParent(), false);
        return footView;
    }

    @Override
    public void hideStateView() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showOrderInfo(OrderInfo orderInfo) {
        LogUtils.e("创建订单成功--->");

        if (orderInfo != null) {
            orderInfo.setMoney(0.01f);
            orderInfo.setName("说说英语微课购买");
        }

        new IAliPay1Impl(ConfirmOrderActivity.this).pay(orderInfo, new IPayCallback() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                RxBus.get().post(OrderConstant.PAY_SUCCESS, orderInfo != null ? orderInfo.getPay_order_sn() : "");
            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                Looper.prepare();
                ToastUtils.showLong("支付失败");
                Looper.loop();
            }
        });
    }

    @Override
    public void showOrderPayResult(ResultInfo resultInfo) {

    }

    /**
     * 购物车中移除已经购买的
     */
    public void removeBuyItemInCart() {
        if (orderList != null && orderList.size() > 0) {
            ShoppingHelper.deleteCourseListByUser(UserInfoHelper.getUserInfo().getUid(), orderList);
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(OrderConstant.PAY_SUCCESS)
            }
    )
    public void paySuccess(String payOrderSn) {
        removeBuyItemInCart();
        finish();
    }
}
