package com.yc.english.union.view.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
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
import com.yc.english.group.view.adapter.GroupGroupAdapter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionListContract1;
import com.yc.english.union.presenter.UnionListPresenter1;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;

/**
 * Created by wanglin  on 2017/9/11 17:32.
 */

public class UnionFragment extends BaseFragment<UnionListPresenter1> implements UnionListContract1.View {
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
        mPresenter = new UnionListPresenter1(getActivity(), UnionFragment.this);
        initRecycleView();
        getData();

        initListener();

    }

    private void initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.primary), ContextCompat.getColor(getActivity(), R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getData();
            }
        });

        adapter.setOnJoinListener(new GroupGroupAdapter.OnJoinListener() {
            @Override
            public void onJoin(ClassInfo classInfo) {
                int result = SPUtils.getInstance().getInt(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid());
                if (!UserInfoHelper.isGotoLogin(getActivity())) {
                    if (result == 1) {
                        setMode(classInfo);
                        RongIM.getInstance().startGroupChat(getActivity(), classInfo.getClass_id(), classInfo.getClassName());
                    } else {
                        mPresenter.isUnionMember(classInfo);
                    }
                }
            }
        });

    }

    private void setMode(ClassInfo classInfo) {

        GroupApp.setMyExtensionModule(false, false);

        RongIM.getInstance().startGroupChat(getActivity(), classInfo.getClass_id(), classInfo.getClassName());
    }

    private void getData() {
        if (UserInfoHelper.getUserInfo() != null) {
            String uid = UserInfoHelper.getUserInfo().getUid();
            mPresenter.getUnionList1(uid, mType + "", "1");
        }

    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupGroupAdapter(getContext(), true, null);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showIsMember(int is_member, final ClassInfo classInfo) {
        SPUtils.getInstance().put(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid(), is_member);

        if (is_member == 1) {//已经是班群成员
            setMode(classInfo);
            RongIM.getInstance().startGroupChat(getActivity(), classInfo.getClass_id(), classInfo.getClassName());
        } else {
            final AlertDialog dialog = new AlertDialog(getActivity());
            dialog.setDesc("是否申请加入该班群?");
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.applyJoinGroup(UserInfoHelper.getUserInfo().getUid(), classInfo);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void showUnionList1(List<ClassInfo> classInfos) {
        if (classInfos != null && classInfos.size() > 0) {
            adapter.setData(classInfos);

        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
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
        getData();
    }

    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(swipeRefreshLayout, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(swipeRefreshLayout);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(swipeRefreshLayout);
    }
}