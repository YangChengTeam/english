package com.yc.soundmark.category.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.StateView;
import com.yc.soundmark.base.widget.MainToolBar;
import com.yc.soundmark.category.adapter.WeiKeInfoItemAdapter;
import com.yc.soundmark.category.contract.CategoryMainContract;
import com.yc.soundmark.category.model.domain.WeiKeCategory;
import com.yc.soundmark.category.presenter.CategoryMainPresenter;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;

import yc.com.base.BaseActivity;


/**
 * 微课单元列表
 */

public class WeikeUnitActivity extends BaseActivity<CategoryMainPresenter> implements CategoryMainContract.View {


    private MainToolBar mainToolbar;

    private RecyclerView categoryRecyclerView;

    private SmartRefreshLayout swipeRefreshLayout;


    private WeiKeInfoItemAdapter mWeiKeInfoItemAdapter;

    private String type = "";

    private String pid = "";

    private int page = 1;

    private int pageSize = 20;
    private StateView stateView;

    @Override
    public void init() {
        initView();
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mPresenter = new CategoryMainPresenter(this, this);
        final Intent intent = getIntent();
        if (intent != null) {
            pid = intent.getStringExtra("category_id");
        }

        mainToolbar.showNavigationIcon();
        mainToolbar.setTitle("微课学习");
        mainToolbar.setRightContainerVisible(false);

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mWeiKeInfoItemAdapter = new WeiKeInfoItemAdapter(null, type);
        categoryRecyclerView.setAdapter(mWeiKeInfoItemAdapter);
        categoryRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 6, 6));


        mWeiKeInfoItemAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent1 = new Intent(WeikeUnitActivity.this, WeiKeDetailActivity.class);

            intent1.putExtra("pid", mWeiKeInfoItemAdapter.getItem(position).getId());
            startActivity(intent1);
        });

        mWeiKeInfoItemAdapter.setOnLoadMoreListener(() -> getData(false), categoryRecyclerView);

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = categoryRecyclerView.getChildAt(0);
                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0);
                }
            }
        });

        getData(false);
        initRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    private void initView() {
        mainToolbar = findViewById(R.id.main_toolbar);
        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        stateView = findViewById(R.id.stateView);
    }

    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式
        swipeRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        swipeRefreshLayout.setPrimaryColorsId(R.color.primaryDark);
        swipeRefreshLayout.setEnableLoadMore(false);
//        swipeRefreshLayout.autoRefresh();
        swipeRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getData(true);
        });
    }

    @Override
    public void showNoNet() {

        stateView.showNoNet(swipeRefreshLayout, HttpConfig.NET_ERROR, v -> {
            page = 1;
            getData(false);
        });

        swipeRefreshLayout.finishRefresh();

    }

    @Override
    public void showNoData() {

        stateView.showNoData(swipeRefreshLayout);
        swipeRefreshLayout.finishRefresh();

    }

    @Override
    public void showLoading() {
        stateView.showLoading(swipeRefreshLayout);
    }


    private void getData(boolean isRefresh) {
        mPresenter.getCategoryInfos(page, pageSize, pid, isRefresh);
    }

    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {

            mWeiKeInfoItemAdapter.setNewData(weiKeCategoryList);
        } else {
            mWeiKeInfoItemAdapter.addData(weiKeCategoryList);
        }
        if (pageSize == weiKeCategoryList.size()) {
            page++;
            mWeiKeInfoItemAdapter.loadMoreComplete();
        } else {
            mWeiKeInfoItemAdapter.loadMoreEnd();
        }

        swipeRefreshLayout.finishRefresh();
    }

    @Override
    public void hide() {
        stateView.hide();

    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

}
