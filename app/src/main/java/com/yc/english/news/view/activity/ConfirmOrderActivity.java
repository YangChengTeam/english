package com.yc.english.news.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.news.adapter.OrderItemAdapter;
import com.yc.english.news.contract.OrderContract;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.news.presenter.OrderPresenter;
import com.yc.english.news.view.widget.SpaceItemDecoration;
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void init() {
        mToolbar.setTitle("确认订单");
        mToolbar.showNavigationIcon();

        Intent intent = getIntent();
        ArrayList<CourseInfo> list = intent.getParcelableArrayListExtra("goods_list");

        mPresenter = new OrderPresenter(this, this);

        mTotalPrice.setText(intent.getFloatExtra("total_price", 0) + "");
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        mOrderItemAdapter = new OrderItemAdapter(list);
        mOrderItemAdapter.setFooterView(getFootView());
        mOrderRecyclerView.setAdapter(mOrderItemAdapter);

        List<OrderGood> orderGoodsList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                OrderGood orderGood = new OrderGood();
                orderGood.setGoodId(list.get(i).getGoodId());
                orderGood.setNum(1);
                orderGoodsList.add(orderGood);
            }
        }

        orderParams = new OrderParams();
        orderParams.setPriceTotal(intent.getFloatExtra("total_price", 0) + "");
        orderParams.setPayWayName("alipay");
        orderParams.setGoodsList(orderGoodsList);

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
    public void showOrderInfo(ResultInfo data) {
        LogUtils.e("创建订单成功--->");
    }
}
