package com.yc.junior.english.group.view.activitys.teacher;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.group.contract.GroupPublishTaskListContract;
import com.yc.junior.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.junior.english.group.presenter.GroupPublishTaskListPresenter;
import com.yc.junior.english.group.view.adapter.GroupTaskListAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 08:57.
 */

public class GroupPublishTaskListActivity extends FullScreenActivity<GroupPublishTaskListPresenter> implements GroupPublishTaskListContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.stateView)
    StateView stateView;
    private GroupTaskListAdapter adapter;
    private String targetId;

    @Override
    public void init() {
        mPresenter = new GroupPublishTaskListPresenter(this, this);
        if (getIntent() != null) {
            targetId = getIntent().getStringExtra("targetId");

            mPresenter.getPublishTaskList("", targetId);

        }

        mToolbar.setTitle(getString(R.string.my_publish));

        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupTaskListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);

    }




    @Override
    public int getLayoutId() {
        return R.layout.group_activity_publish_task;
    }


    @Override
    public void showPublishTaskList(List<TaskAllInfoWrapper.TaskAllInfo> list) {
        adapter.setData(list);
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.getPublishTaskList("", targetId);

            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mRecyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(mRecyclerView);
    }
}
