package com.yc.junior.english.composition.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.MainToolBar;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.composition.adapter.EssayItemAdapter;
import com.yc.junior.english.composition.contract.EssayContract;
import com.yc.junior.english.composition.model.bean.CompositionInfo;
import com.yc.junior.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.junior.english.composition.presenter.EssayPresenter;
import com.yc.junior.english.composition.widget.MyLoadMoreView;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


/**
 * Created by wanglin  on 2019/3/25 10:58.
 */
public class CompositionMoreActivity extends FullScreenActivity<EssayPresenter> implements EssayContract.View {
    @BindView(R.id.toolbar)
    MainToolBar toolbar;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.weixin_fragment_recyclerView)
    RecyclerView weixinFragmentRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SmartRefreshLayout swipeRefreshLayout;
    private EssayItemAdapter essayAdapter;
    private String attrid;

    private int page = 1;
    private int PAGESIZE = 10;

    public static void startActivity(Context context, String title, String attrid) {
        Intent intent = new Intent(context, CompositionMoreActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("attrid", attrid);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_more;
    }

    @Override
    public void init() {
        if (getIntent() != null) {
            String title = getIntent().getStringExtra("title");
            attrid = getIntent().getStringExtra("attrid");
            toolbar.setTitle(title);
        }
        toolbar.showNavigationIcon();
        toolbar.init(this);
        mPresenter = new EssayPresenter(this, this);
        getData(false);

        weixinFragmentRecyclerView.setPadding(ScreenUtil.dip2px(this, 10f), ScreenUtil.dip2px(this, 10f), ScreenUtil.dip2px(this, 10f), ScreenUtil.dip2px(this, 10f));
        weixinFragmentRecyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        weixinFragmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        essayAdapter = new EssayItemAdapter(null, true);
        essayAdapter.setLoadMoreView(new MyLoadMoreView());
        weixinFragmentRecyclerView.setAdapter(essayAdapter);
        weixinFragmentRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 8));
        initRefresh();
        initListener();


    }

    private void initListener() {
        essayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CompositionInfo compositionInfo = essayAdapter.getItem(position);
                if (compositionInfo != null) {
                    CompositionDetailActivity.startActivity(CompositionMoreActivity.this, compositionInfo.getId());
                    mPresenter.statisticsReadCount(compositionInfo.getId());
                }
            }
        });

        weixinFragmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = weixinFragmentRecyclerView.getChildAt(0);
                if (child.getTop() < 0) {
                    weixinFragmentRecyclerView.setPadding(weixinFragmentRecyclerView.getPaddingLeft(), 0, weixinFragmentRecyclerView.getPaddingRight(), weixinFragmentRecyclerView.getPaddingBottom());
                } else {
                    weixinFragmentRecyclerView.setPadding(weixinFragmentRecyclerView.getPaddingLeft(), ScreenUtil.dip2px(CompositionMoreActivity.this, 10f), weixinFragmentRecyclerView.getPaddingRight(), weixinFragmentRecyclerView.getPaddingBottom());
                }
            }
        });

        essayAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, weixinFragmentRecyclerView);
    }

    @Override
    public void showCompositionInfos(List<CompositionInfo> compositionInfos) {
        if (page == 1) {

            essayAdapter.setNewData(compositionInfos);
        } else {
            essayAdapter.addData(compositionInfos);
        }
        if (compositionInfos.size() == PAGESIZE) {
            essayAdapter.loadMoreComplete();
            page++;
        } else {
            essayAdapter.loadMoreEnd();
        }

        swipeRefreshLayout.finishRefresh();
    }

    @Override
    public void showCompositionIndexInfo(CompositionInfoWrapper data) {

    }

    @Override
    public void showBanner(List<String> images) {

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
    public void hide() {
        stateView.hide();
        swipeRefreshLayout.finishRefresh();
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
                getData(false);
            }
        });
        swipeRefreshLayout.finishRefresh();
    }

    private void getData(boolean isRefresh) {
        mPresenter.getCompositionInfos(attrid, page, PAGESIZE, isRefresh);
    }
}
