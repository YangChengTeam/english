package com.yc.english.union.view.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionCommonListContract;
import com.yc.english.union.presenter.UnionCommonListPresenter;
import com.yc.english.union.view.adapter.GroupUnionAdapter;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;

/**
 * Created by wanglin  on 2017/9/11 17:32.
 */

public class UnionAllFragment extends BaseFragment<UnionCommonListPresenter> implements UnionCommonListContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.stateView)
    StateView stateView;

    private GroupUnionAdapter adapter;

    private int mType;
    private int page = 1;
    private int page_size = 10;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_union_list;
    }

    public void setType(int type) {
        this.mType = type;
    }

    @Override
    public void init() {
        mPresenter = new UnionCommonListPresenter(getActivity(), UnionAllFragment.this);
        if (getArguments() != null) {
            List<ClassInfo> classInfos = getArguments().getParcelableArrayList("classInfos");
            initRecycleView(classInfos);
        }
        initListener();

    }

    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData(false, false);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                getData(true, false);
            }
        }, recyclerView);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ClassInfo classInfo = (ClassInfo) adapter.getItem(position);

                int result = SPUtils.getInstance().getInt(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid());
                if (result == 1) {
                    setMode(classInfo);
                } else {
                    mPresenter.isGroupMember(classInfo);
                }
            }

        });
    }


    private void setMode(ClassInfo classInfo) {

        GroupApp.setMyExtensionModule(false, false);

        RongIM.getInstance().startGroupChat(getActivity(), classInfo.getClass_id(), classInfo.getClassName());
    }

    private void getData(boolean isLoadMore, boolean isFirst) {
        if (isLoadMore) {
            page++;
        }
        mPresenter.getUnionList(mType + "", page, page_size, isFirst);
    }


    private void initRecycleView(List<ClassInfo> classInfos) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupUnionAdapter(null);
        recyclerView.setAdapter(adapter);
        adapter.setNewData(classInfos);
        hideStateView();
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
    }


    @Override
    public void showUnionList(List<ClassInfo> data, int page, boolean isFitst) {

        if (page == 1) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
        }
        if (data.size() == page_size) {
            adapter.loadMoreComplete();
        } else {
            adapter.loadMoreEnd();
        }

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showMemberList(List<StudentInfo> list) {
    }

    @Override
    public void showMyGroupList(List<ClassInfo> classInfos) {
    }

    @Override
    public void showCommonClassList(List<ClassInfo> list) {

    }

    @Override
    public void showIsMember(int is_member, final ClassInfo classInfo) {
        SPUtils.getInstance().put(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid(), is_member);

        if (is_member == 1) {//已经是班群成员
            setMode(classInfo);
        } else {
            final AlertDialog dialog = new AlertDialog(getActivity());
            dialog.setDesc("是否申请加入该公会?");
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.applyJoinGroup(classInfo);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
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
        page = 1;
        getData(false, false);
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

}