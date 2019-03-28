package com.yc.soundmark.category.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.english.R;
import com.yc.english.base.view.StateView;
import com.yc.soundmark.base.constant.Config;
import com.yc.soundmark.base.widget.MainToolBar;
import com.yc.soundmark.category.adapter.CategoryMainAdapter;
import com.yc.soundmark.category.contract.CategoryMainContract;
import com.yc.soundmark.category.model.domain.WeiKeCategory;
import com.yc.soundmark.category.presenter.CategoryMainPresenter;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;
import java.util.Map;

import yc.com.base.BaseActivity;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class CategoryActivity extends BaseActivity<CategoryMainPresenter> implements CategoryMainContract.View, OnAdvStateListener {

    private RecyclerView categoryRecyclerView;

    private MainToolBar mainToolbar;

    private SmartRefreshLayout swipeRefreshLayout;

    private FrameLayout bottomContainer;


    private CategoryMainAdapter categoryMainAdapter;
    private int page = 1;

    private int PAGE_SIZE = 20;
    private RecyclerView.ItemDecoration itemDecoration;
    private StateView stateView;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void init() {
        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        mainToolbar = findViewById(R.id.main_toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        bottomContainer = findViewById(R.id.bottom_container);
        stateView = findViewById(R.id.stateView);

        mPresenter = new CategoryMainPresenter(this, this);

        mainToolbar.init(this, null);
        mainToolbar.setTitle(getString(R.string.main_category));
        mainToolbar.setRightContainerVisible(false);
//        mainToolbar.setTvRightTitleAndIcon(getString(R.string.main_category), R.mipmap.diandu);

        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND)))
            AdvDispatchManager.getManager().init(this, AdvType.BANNER, bottomContainer, null, Config.TENCENT_ADV_ID, Config.BANNER_TOP_ADV_ID, this);
        getData(false);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryRecyclerView.setHasFixedSize(true);

        categoryMainAdapter = new CategoryMainAdapter(null);

        categoryRecyclerView.setAdapter(categoryMainAdapter);

        itemDecoration = new ItemDecorationHelper(this, 6, 6);

        categoryRecyclerView.addItemDecoration(itemDecoration);


        categoryMainAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, categoryRecyclerView);


        categoryMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CategoryActivity.this, WeikeUnitActivity.class);
                intent.putExtra("category_id", categoryMainAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = recyclerView.getChildAt(0);

                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(CategoryActivity.this, 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(CategoryActivity.this, 6), ScreenUtil.dip2px(CategoryActivity.this, 6), 0, 0);
                }
            }
        });
        initRefresh();


    }


    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {
            categoryMainAdapter.setNewData(weiKeCategoryList);
        } else {
            categoryMainAdapter.addData(weiKeCategoryList);
        }

        if (weiKeCategoryList.size() == PAGE_SIZE) {
            page++;
            categoryMainAdapter.loadMoreComplete();
        } else {
            categoryMainAdapter.loadMoreEnd();
        }
        swipeRefreshLayout.finishRefresh();
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(swipeRefreshLayout);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(swipeRefreshLayout);
        swipeRefreshLayout.finishRefresh();

    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(swipeRefreshLayout, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                getData(false);
            }
        });
        swipeRefreshLayout.finishRefresh();
    }

    private void getData(boolean isRefresh) {
        mPresenter.getCategoryInfos(page, PAGE_SIZE, "0", isRefresh);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemDecoration != null) {
            categoryRecyclerView.removeItemDecoration(itemDecoration);
        }
    }


    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式
        swipeRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        swipeRefreshLayout.setPrimaryColorsId(R.color.primaryDark);
        swipeRefreshLayout.setEnableLoadMore(false);
//        swipeRefreshLayout.autoRefresh();
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(true);
            }
        });
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
