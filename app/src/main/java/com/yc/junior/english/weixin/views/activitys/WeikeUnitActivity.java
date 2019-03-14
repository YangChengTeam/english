package com.yc.junior.english.weixin.views.activitys;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.news.view.activity.NewsDetailActivity;
import com.yc.junior.english.news.view.activity.NewsWeiKeDetailActivity;
import com.yc.junior.english.weixin.contract.WeiKeContract;
import com.yc.junior.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.junior.english.weixin.model.domain.WeiKeInfo;
import com.yc.junior.english.weixin.presenter.WeiKePresenter;
import com.yc.junior.english.weixin.views.adapters.WeiKeInfoItemAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 微课单元列表
 */

public class WeikeUnitActivity extends FullScreenActivity<WeiKePresenter> implements WeiKeContract.View {

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;


    @BindView(R.id.rv_category_list)
    RecyclerView mCategoryListRecyclerView;

    private WeiKeInfoItemAdapter mWeiKeInfoItemAdapter;

    private String type = "";

    private String pid = "";

    private int page = 1;

    private int pageSize = 20;

    @Override
    public void init() {
        mPresenter = new WeiKePresenter(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type") + "";
            pid = intent.getStringExtra("pid") + "";
        }

        mToolbar.setTitle("微课学习");
        mToolbar.showNavigationIcon();

        mCategoryListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mWeiKeInfoItemAdapter = new WeiKeInfoItemAdapter(null, type);
        mCategoryListRecyclerView.setAdapter(mWeiKeInfoItemAdapter);

        mWeiKeInfoItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type.equals("8")) {
                    Intent intent = new Intent(WeikeUnitActivity.this, NewsWeiKeDetailActivity.class);
                    intent.putExtra("id",mWeiKeInfoItemAdapter.getData().get(position).getId());
                    //intent.putExtra("info", mWeiKeInfoItemAdapter.getData().get(position));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WeikeUnitActivity.this, NewsDetailActivity.class);
                    intent.putExtra("id",mWeiKeInfoItemAdapter.getData().get(position).getId());
                    startActivity(intent);
                }
            }
        });

        mWeiKeInfoItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getWeiKeInfoList(pid, page + "");
            }
        }, mCategoryListRecyclerView);

        mPresenter.getWeiKeInfoList(pid, page + "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.weike_category_list;
    }

    @Override
    public void hide() {
        mLoadingStateView.hide();
    }

    @Override
    public void showNoNet() {
        mLoadingStateView.showNoNet(mCategoryListRecyclerView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeiKeInfoList(pid, page + "");
            }
        });
    }

    @Override
    public void showNoData() {
        mLoadingStateView.showNoData(mCategoryListRecyclerView);
    }

    @Override
    public void showLoading() {
        mLoadingStateView.showLoading(mCategoryListRecyclerView);
    }


    @Override
    public void showWeikeCategoryList(WeiKeCategoryWrapper categoryWrapper) {

    }

    @Override
    public void showWeiKeInfoList(List<WeiKeInfo> list) {
        if (page == 1) {
            mWeiKeInfoItemAdapter.setNewData(list);
        } else {
            mWeiKeInfoItemAdapter.addData(list);
        }
        if (list.size() == pageSize) {
            page++;
            mWeiKeInfoItemAdapter.loadMoreComplete();
        } else {
            mWeiKeInfoItemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void fail() {
        mWeiKeInfoItemAdapter.loadMoreFail();
    }

    @Override
    public void end() {
        mWeiKeInfoItemAdapter.loadMoreEnd();
    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
