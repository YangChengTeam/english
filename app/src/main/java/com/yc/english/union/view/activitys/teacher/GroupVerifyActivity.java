package com.yc.english.union.view.activitys.teacher;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupApplyVerifyContract;
import com.yc.english.group.listener.OnItemClickListener;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupApplyVerifyPresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.view.adapter.GroupVerifyAdapter;

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
    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    private GroupVerifyAdapter adapter;

    private String flag;

    @Override
    public void init() {
        mPresenter = new GroupApplyVerifyPresenter(this, this);
        mToolbar.setTitle(getString(R.string.friend_verify));
        mToolbar.showNavigationIcon();
        if (getIntent() != null) {
            flag = getIntent().getStringExtra("flag");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupVerifyAdapter(this, null);
        recyclerView.setAdapter(adapter);
        getData();

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_friend;
    }


    @Override
    public void showVerifyList(List<StudentInfo> list) {
        adapter.setData(list);
        initListener();
    }

    @Override
    public void showApplyResult(String data) {
        mHolder.setVisible(R.id.m_tv_accept, false);
        mHolder.setVisible(R.id.m_tv_already_add, true);

    }

    private BaseViewHolder mHolder;


    private void initListener() {
        adapter.setOnItemClickListener(new OnItemClickListener<StudentInfo>() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position, StudentInfo studentInfo) {
                mPresenter.acceptApply(studentInfo);
                mHolder = holder;
            }
        });

    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    private void getData() {
        String uid = UserInfoHelper.getUserInfo().getUid();
        mPresenter.getMemberList(this, "", "0", uid, flag);
    }
}
