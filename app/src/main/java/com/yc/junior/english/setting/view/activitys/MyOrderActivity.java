package com.yc.junior.english.setting.view.activitys;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.contract.MyContract;
import com.yc.junior.english.setting.model.bean.MyOrderInfo;
import com.yc.junior.english.setting.model.bean.ScoreInfo;
import com.yc.junior.english.setting.presenter.MyPresenter;
import com.yc.junior.english.setting.view.adapter.MyOrderItemAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/11/10.
 */

public class MyOrderActivity extends FullScreenActivity<MyPresenter> implements MyContract.View, OnRefreshListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;


    @BindView(R.id.rv_order_list)
    RecyclerView mOrderListRecyclerView;
    @BindView(R.id.pull_to_refresh)
    SmartRefreshLayout swipeLayout;

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

        swipeLayout.setRefreshHeader(new ClassicsHeader(this));
        swipeLayout.setPrimaryColorsId(R.color.primaryDark);
        swipeLayout.setEnableLoadMore(false);
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
//        myOrderItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(MyOrderActivity.this, NewsWeiKeDetailActivity.class);
//                intent.putExtra("id",myOrderItemAdapter.getData().get(position).getId());
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void hide() {
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
        if (swipeLayout != null) {
            swipeLayout.finishRefresh();
        }
    }

    @Override
    public void showScoreResult(ScoreInfo data) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        mPresenter.getMyOrderInfoList(currentPage, pageSize);
    }
}
