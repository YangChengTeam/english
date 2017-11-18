package com.yc.english.union.view.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.view.adapter.GroupGroupAdapter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionCommonListContract;
import com.yc.english.union.presenter.UnionCommonListPresenter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/9/11 17:32.
 */

public class UnionFragment extends BaseFragment<UnionCommonListPresenter> implements UnionCommonListContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stateView)
    StateView stateView;

    private GroupGroupAdapter adapter;

    private int mType;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_union_list;
    }


    public void setType(int type) {
        this.mType = type;
    }


    @Override
    public void init() {
        mPresenter = new UnionCommonListPresenter(getActivity(), UnionFragment.this);
        initRecycleView();
        getData();
        initListener();

    }

    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }


    private void getData() {
        if (UserInfoHelper.getUserInfo() != null) {
            String uid = UserInfoHelper.getUserInfo().getUid();
            mPresenter.getMyGroupList(uid, mType + "", "1");
        }

    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupGroupAdapter(getActivity(), true, null);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showMyGroupList(List<ClassInfo> classInfos) {
        Collections.sort(classInfos, new Comparator<ClassInfo>() {
            @Override
            public int compare(ClassInfo o1, ClassInfo o2) {
                return Integer.parseInt(o2.getCount()) - Integer.parseInt(o1.getCount());
            }
        });

        adapter.setData(classInfos);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showCommonClassList(List<ClassInfo> list) {

    }

    @Override
    public void showIsMember(int is_member, ClassInfo classInfo) {

    }

    @Override
    public void showUnionList(List<ClassInfo> data, int page, boolean isFitst) {

    }

    @Override
    public void showMemberList(List<StudentInfo> list) {

    }

    @Override
    public void showLoadingDialog(String msg) {
        ((BaseActivity) getActivity()).showLoadingDialog(msg);
    }

    @Override
    public void dismissLoadingDialog() {
        ((BaseActivity) getActivity()).dismissLoadingDialog();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GROUP_LIST)
            }
    )
    public void getList(String group) {
        getData();
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
                getData();
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
}