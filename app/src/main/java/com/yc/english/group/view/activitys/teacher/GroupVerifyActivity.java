package com.yc.english.group.view.activitys.teacher;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupApplyVerifyContract;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupApplyVerifyPresenter;
import com.yc.english.group.view.adapter.GroupVerifyAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/29 11:58.
 * 好友验证
 */

public class GroupVerifyActivity extends FullScreenActivity<GroupApplyVerifyPresenter> implements GroupApplyVerifyContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateView)
    StateView stateView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private GroupVerifyAdapter groupVerifyAdapter;

    private String type;

    private int page = 1;
    private int page_size = 10;


    @Override
    public void init() {
        mPresenter = new GroupApplyVerifyPresenter(this, this);
        mToolbar.setTitle(getString(R.string.friend_verify));
        mToolbar.showNavigationIcon();
        if (getIntent() != null) {
            type = getIntent().getStringExtra("type");
            List<StudentInfo> studentList = getIntent().getParcelableArrayListExtra("studentList");

        }
        initRecycleView();
        initListener();
        getData(false,true);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupVerifyAdapter = new GroupVerifyAdapter(null);
        recyclerView.setAdapter(groupVerifyAdapter);

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_friend;
    }


    @Override
    public void showVerifyList(List<StudentInfo> list) {

        if (page == 1) {
            groupVerifyAdapter.setNewData(list);
        } else {
            groupVerifyAdapter.addData(list);
        }
        if (list.size() == page_size) {
            groupVerifyAdapter.loadMoreComplete();
        } else {
            groupVerifyAdapter.loadMoreEnd();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void showApplyResult(String data, int position) {
        groupVerifyAdapter.getViewByPosition(recyclerView, position, R.id.m_tv_accept).setVisibility(View.GONE);
        groupVerifyAdapter.getViewByPosition(recyclerView, position, R.id.m_tv_already_add).setVisibility(View.VISIBLE);

    }


    private void initListener() {
//        groupVerifyAdapter.setEnableLoadMore(false);
//        groupVerifyAdapter.disableLoadMoreIfNotFullPage(recyclerView);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData(false, false);
            }
        });


        groupVerifyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(true, false);
            }
        }, recyclerView);


        groupVerifyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StudentInfo studentInfo = (StudentInfo) adapter.getItem(position);
                mPresenter.acceptApply(studentInfo, position);
                return false;
            }
        });

    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false, true);
            }
        });
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerView);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerView);
    }

    private void getData(boolean isLoadMore, boolean isFirst) {
        String uid = UserInfoHelper.getUserInfo().getUid();
        if (isLoadMore) {
            page++;
        }
        mPresenter.getMemberList("", page, page_size, "0", uid, type, isFirst);
    }

}
