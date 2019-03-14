package com.yc.junior.english.weixin.views.fragments;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.news.view.activity.NewsDetailActivity;
import com.yc.junior.english.weixin.contract.CourseContract;
import com.yc.junior.english.weixin.model.domain.CourseInfo;
import com.yc.junior.english.weixin.presenter.CoursePresenter;
import com.yc.junior.english.weixin.views.adapters.CourseAdapter;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;


/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseMoreFragment extends BaseFragment<CoursePresenter> implements CourseContract.View {

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    private String type;
    private int page = 1;
    private int pageSize = 20;

    private CourseAdapter mCourseAdapter;

    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshSwipeRefreshLayout;


    public void setType(String type) {
        this.type = type;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.GRADE_REFRESH)
            }
    )
    public void refresh(String tag) {
        page = 1;
        mPresenter.getWeiXinList(type, page + "", pageSize + "");
    }

    @Override
    public void init() {

        mPresenter = new CoursePresenter(getActivity(), this);

        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCourseAdapter = new CourseAdapter(null);
        mCourseRecyclerView.setAdapter(mCourseAdapter);

        mCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("info", mCourseAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCourseAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getWeiXinList(type, page + "", pageSize + "");
            }
        }, mCourseRecyclerView);

        mPresenter.getWeiXinList(type, page + "", pageSize + "");
        mRefreshSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.primaryDark), ContextCompat.getColor(getActivity(), R.color.primaryDark));
        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh("mRefreshSwipeRefreshLayout");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course;
    }

    @Override
    public void hide() {
        mLoadingStateView.hide();
    }

    @Override
    public void showNoNet() {
        mLoadingStateView.showNoNet(mRefreshSwipeRefreshLayout, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeiXinList(type, page + "", pageSize + "");
            }
        });
    }

    @Override
    public void showNoData() {
        mLoadingStateView.showNoData(mRefreshSwipeRefreshLayout);
    }

    @Override
    public void showLoading() {
        if (!mRefreshSwipeRefreshLayout.isRefreshing() || (mCourseAdapter.getData() == null || mCourseAdapter.getData()
                .size() == 0))
            mLoadingStateView.showLoading(mCourseRecyclerView);
    }

    @Override
    public void showWeixinList(List<CourseInfo> list) {
        if (page == 1) {
            mCourseAdapter.setNewData(list);
        } else {
            mCourseAdapter.addData(list);
        }
        if (list.size() == pageSize) {
            page++;
            mCourseAdapter.loadMoreComplete();
        } else {
            mCourseAdapter.loadMoreEnd();
        }
        mRefreshSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void fail() {
        mCourseAdapter.loadMoreFail();
    }


    @Override
    public void end() {
        mCourseAdapter.loadMoreEnd();
    }


}
