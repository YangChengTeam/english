package com.yc.english.weixin.views.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.weixin.contract.CourseContract;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.presenter.CoursePresenter;
import com.yc.english.weixin.views.adapters.CourseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class CourseTypeActivity extends FullScreenActivity<CoursePresenter> implements CourseContract.View {

    @BindView(R.id.weixin_fragment_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.swipeRefreshLayout)
    SmartRefreshLayout mRefreshSwipeRefreshLayout;

    private CourseAdapter mCourseAdapter;
    private int page = 1;
    private int pageSize = 20;

    @Override
    public void init() {
        mPresenter = new CoursePresenter(this, this);
        mToolbar.setTitle("爱学习");
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCourseAdapter = new CourseAdapter(null);
        mRecyclerView.setHasFixedSize(true);//item高度一致，设置属性避免requestLayout 浪费资源
        mRecyclerView.setAdapter(mCourseAdapter);

        mCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MobclickAgent.onEvent(CourseTypeActivity.this, "fine_read_click", "精品推荐");
                Intent intent = new Intent(CourseTypeActivity.this, NewsDetailActivity.class);
                intent.putExtra("info", mCourseAdapter.getData().get(position));
                startActivity(intent);
                mPresenter.statisticsNewsCount(mCourseAdapter.getData().get(position).getId());
            }
        });

        mCourseAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, mRecyclerView);

        getData();

        initRefresh();

    }

    private void initRefresh() {
        mRefreshSwipeRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mRefreshSwipeRefreshLayout.setPrimaryColorsId(R.color.primaryDark);
        mRefreshSwipeRefreshLayout.setEnableLoadMore(false);
        mRefreshSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh("mRefreshSwipeRefreshLayout");
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_more;
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mRecyclerView);
    }

    @Override
    public void showLoading() {
        if ((mCourseAdapter.getData() == null || mCourseAdapter.getData()
                .size() == 0))
            stateView.showLoading(mRecyclerView);

        if (mRefreshSwipeRefreshLayout != null) {
            mRefreshSwipeRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void showWeixinList(List<CourseInfo> list) {
        if (page == 1) {
            mCourseAdapter.setNewData(list);
        } else {
            mCourseAdapter.addData(list);
        }
        if (list.size() >= pageSize) {
            page++;
            mCourseAdapter.loadMoreComplete();
        } else {
            mCourseAdapter.loadMoreEnd();
        }
        if (mRefreshSwipeRefreshLayout != null) {
            mRefreshSwipeRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void fail() {
        mCourseAdapter.loadMoreFail();
        if (mRefreshSwipeRefreshLayout != null) {
            mRefreshSwipeRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void end() {
        mCourseAdapter.loadMoreEnd();
        if (mRefreshSwipeRefreshLayout != null) {
            mRefreshSwipeRefreshLayout.finishRefresh();
        }
    }

    private void getData() {
        mPresenter.getWeiXinList("syntax", page, pageSize);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.GRADE_REFRESH)
            }
    )
    public void refresh(String tag) {
        page = 1;
        getData();
    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
