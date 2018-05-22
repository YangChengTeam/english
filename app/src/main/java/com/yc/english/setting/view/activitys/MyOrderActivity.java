package com.yc.english.setting.view.activitys;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.view.activity.NewsWeiKeDetailActivity;
import com.yc.english.setting.contract.MyContract;
import com.yc.english.setting.model.bean.MyOrderInfo;
import com.yc.english.setting.model.bean.ScoreInfo;
import com.yc.english.setting.presenter.MyPresenter;
import com.yc.english.setting.view.adapter.MyOrderItemAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/11/10.
 */

public class MyOrderActivity extends FullScreenActivity<MyPresenter> implements MyContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.rv_order_list)
    RecyclerView mOrderListRecyclerView;

    private MyOrderItemAdapter myOrderItemAdapter;

    private int currentPage = 1;

    private int pageSize = 20;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    public void init() {
        mToolbar.setTitle("我的订单");
        mToolbar.showNavigationIcon();

        swipeLayout.setColorSchemeResources(
                R.color.primary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                R.color.primaryDark);
        swipeLayout.setOnRefreshListener(this);


        mPresenter = new MyPresenter(this, this);
        mOrderListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myOrderItemAdapter = new MyOrderItemAdapter(null);
        mOrderListRecyclerView.setAdapter(myOrderItemAdapter);

        myOrderItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMyOrderInfoList(currentPage, pageSize);
            }
        }, mOrderListRecyclerView);

        mPresenter.getMyOrderInfoList(currentPage, pageSize);
        myOrderItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyOrderActivity.this, NewsWeiKeDetailActivity.class);
                intent.putExtra("id",myOrderItemAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(swipeLayout, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getMyOrderInfoList(currentPage, pageSize);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(swipeLayout);
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {

    }

    @Override
    public void showNoLogin(Boolean flag) {

    }

    @Override
    public void showLoading() {
        mStateView.showLoading(swipeLayout);
    }

    @Override
    public void showMyOrderInfoList(List<MyOrderInfo> list) {
        swipeLayout.setRefreshing(false);
        if (list != null && list.size() > 0) {
            if (currentPage == 1) {
                myOrderItemAdapter.setNewData(list);
            } else {
                myOrderItemAdapter.addData(list);
            }

            currentPage++;
            myOrderItemAdapter.loadMoreComplete();
        } else {
            myOrderItemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showScoreResult(ScoreInfo data) {

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        mPresenter.getMyOrderInfoList(currentPage, pageSize);
    }
}
