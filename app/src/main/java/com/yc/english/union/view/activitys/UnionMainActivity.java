package com.yc.english.union.view.activitys;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.view.activitys.teacher.GroupVerifyActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.union.contract.UnionListContract;
import com.yc.english.union.presenter.UnionListPresenter;
import com.yc.english.union.view.activitys.student.UnionJoinActivity;
import com.yc.english.union.view.activitys.teacher.UnionCreateActivity;
import com.yc.english.union.view.adapter.GroupUnionAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class UnionMainActivity extends FullScreenActivity<UnionListPresenter> implements UnionListContract.View {
    private static final String TAG = "UnionMainActivity";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_empty_container)
    LinearLayout llEmptyContainer;
    @BindView(R.id.ll_data_container)
    LinearLayout llDataContainer;
    @BindView(R.id.sView_loading)
    StateView sViewLoading;
    @BindView(R.id.content_view)
    FrameLayout contentView;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.btn_create_class1)
    Button btnCreateClass1;
    @BindView(R.id.btn_join_class1)
    Button btnJoinClass1;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private GroupUnionAdapter adapter;

    private int page = 1;

    @Override
    public void init() {

        mPresenter = new UnionListPresenter(this, this);
        mToolbar.setTitle(getString(R.string.english_union));

        btnCreateClass.setText(getString(R.string.create_union));
        btnCreateClass1.setText(getString(R.string.create_union));
        btnJoinClass.setText(getString(R.string.join_union));
        btnJoinClass1.setText(getString(R.string.join_union));

        mToolbar.showNavigationIcon();
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(UnionMainActivity.this, GroupVerifyActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupUnionAdapter(null);
        recyclerView.setAdapter(adapter);
        initListener();
        getData(false, true);

    }

    private void initListener() {

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ClassInfo classInfo = (ClassInfo)adapter.getData().get(position);

                int result = SPUtils.getInstance().getInt(classInfo.getClass_id() + "union");
                if (!UserInfoHelper.isGotoLogin(UnionMainActivity.this)) {
                    if (result == 1) {
                        setMode(classInfo);
                    } else {
                        mPresenter.isUnionMember(classInfo);
                    }
                }
            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_light),
                        getResources().getColor(android.R.color.holo_red_light), getResources().getColor(android.R.color.holo_orange_light),
                        getResources().getColor(android.R.color.holo_green_light));
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

    }

    private void setMode(ClassInfo classInfo) {

        GroupApp.setMyExtensionModule(false, false);

        RongIM.getInstance().startGroupChat(this, classInfo.getClass_id(), classInfo.getClassName());
    }

    private void showCreateGuide() {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        final GuidePopupWindow guidePopupWindow = builder.setDelay(1f).setTargetView(btnCreateClass).setCorner(5).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {

                startActivity(new Intent(UnionMainActivity.this, UnionCreateActivity.class));
            }
        })
                .build(this);
        guidePopupWindow.addCustomView(R.layout.guide_create_union_view, R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guidePopupWindow.dismiss();
            }
        });

        guidePopupWindow.setDebug(true);
        guidePopupWindow.show(rootView, "create_union");
    }


    @Override
    public int getLayoutId() {
        return R.layout.group_fragment_list_main;
    }

    @OnClick({R.id.btn_create_class, R.id.btn_create_class1, R.id.btn_join_class, R.id.btn_join_class1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_class:
            case R.id.btn_create_class1:
                goToActivity(UnionCreateActivity.class);

                break;
            case R.id.btn_join_class:
            case R.id.btn_join_class1:
                goToActivity(UnionJoinActivity.class);
                break;
        }

    }

    private void goToActivity(Class activity) {
        if (!UserInfoHelper.isGotoLogin(this)) {
            startActivity(new Intent(this, activity));
        }

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GROUP_LIST)
            }
    )
    public void getList(String group) {
        getData(false, true);
    }


    @Override
    public void showUnionList(List<ClassInfo> classInfos, boolean isLoadMore, boolean isFitst) {

        if (!isLoadMore) {
            if (classInfos != null && classInfos.size() > 0) {
                if (isFitst) {
                    llDataContainer.setVisibility(View.VISIBLE);
                    llEmptyContainer.setVisibility(View.GONE);
                }
                adapter.setNewData(classInfos);
            } else {
                llDataContainer.setVisibility(View.GONE);
                llEmptyContainer.setVisibility(View.VISIBLE);
                hideStateView();
                if (ActivityUtils.isValidContext(this)) {
                    showCreateGuide();
                }
            }
        } else {
            if (classInfos.size() == 10) {
                adapter.loadMoreComplete();
                adapter.addData(classInfos);
            } else {
                adapter.loadMoreEnd();
            }
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void showMemberList(List<StudentInfo> count) {

        if (count != null && count.size() > 0) {
            mToolbar.setMenuIcon(R.mipmap.group65);
        } else {
            mToolbar.setMenuIcon(R.mipmap.group66);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void showIsMember(int is_member, final ClassInfo classInfo) {
        SPUtils.getInstance().put(classInfo.getClass_id() + "union", is_member);

        if (is_member == 1) {//已经是班群成员
            setMode(classInfo);
        } else {
            final AlertDialog dialog = new AlertDialog(this);
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
    public void hideStateView() {
        sViewLoading.hide();
    }

    @Override
    public void showNoNet() {
        sViewLoading.showNoNet(contentView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                getData(false, true);
            }
        });
    }

    @Override
    public void showNoData() {
        hideStateView();
    }

    @Override
    public void showLoading() {
        sViewLoading.showLoading(contentView);
    }

    private void getData(boolean isLoadMore, boolean isFirst) {
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            if (isLoadMore) {
                page++;
            }
            mPresenter.getUnionList("1", "", page, 10, isLoadMore, isFirst);
            mPresenter.getMemberList(this, "", "0", uid);
        } else {
            showUnionList(null, false, true);
        }
    }

}
