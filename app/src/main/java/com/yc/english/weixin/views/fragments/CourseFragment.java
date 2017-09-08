package com.yc.english.weixin.views.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;

import com.yc.english.main.model.domain.Constant;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.weixin.contract.CourseContract;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.presenter.CoursePresenter;
import com.yc.english.weixin.views.adapters.CourseAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseFragment extends BaseFragment<CoursePresenter> implements CourseContract.View {

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    private String type;
    private int page = 1;
    private int pageSize = 20;

    private CourseAdapter mCourseAdapter;


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
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course;
    }

    @Override
    public void hideStateView() {
        mLoadingStateView.hide();
    }

    @Override
    public void showNoNet() {
        mLoadingStateView.showNoNet(mCourseRecyclerView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeiXinList(type, page + "", pageSize + "");
            }
        });
    }

    @Override
    public void showNoData() {
        mLoadingStateView.showNoData(mCourseRecyclerView);
    }

    @Override
    public void showLoading() {
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
