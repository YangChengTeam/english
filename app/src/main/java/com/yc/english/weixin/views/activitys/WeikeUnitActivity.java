package com.yc.english.weixin.views.activitys;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.weixin.contract.CourseContract;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.presenter.CoursePresenter;
import com.yc.english.weixin.views.adapters.WeiKeItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class WeikeUnitActivity extends FullScreenActivity<CoursePresenter> implements CourseContract.View {
    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;

    private WeiKeItemAdapter mWeiKeItemAdapter;
    private String type = "";
    private int page = 1;
    private int pageSize = 20;

    @Override
    public void init() {
        mPresenter = new CoursePresenter(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type") + "";
        }

        mToolbar.setTitle("微课学习");
        mToolbar.showNavigationIcon();
        List<CourseInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setTitle("每日练习英语");
            courseInfo.setPv_num("12");
            courseInfo.setImg("");
            list.add(courseInfo);
        }

        mCourseRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mWeiKeItemAdapter = new WeiKeItemAdapter(list,"7");
        mCourseRecyclerView.setAdapter(mWeiKeItemAdapter);

        mWeiKeItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(WeikeUnitActivity.this, NewsDetailActivity.class);
                intent.putExtra("info", mWeiKeItemAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mWeiKeItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //mPresenter.getWeiXinList(type, page + "", pageSize + "");
            }
        }, mCourseRecyclerView);

        //mPresenter.getWeiXinList(type, page + "", pageSize + "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_activity_course;
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
        if (list == null) {
            mWeiKeItemAdapter.loadMoreEnd();
            return;
        }
        mWeiKeItemAdapter.addData(list);

        if (list.size() == pageSize) {
            page++;
            mWeiKeItemAdapter.loadMoreComplete();
        } else {
            mWeiKeItemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void fail() {
        mWeiKeItemAdapter.loadMoreFail();
    }

    @Override
    public void end() {
        mWeiKeItemAdapter.loadMoreEnd();
    }
}
