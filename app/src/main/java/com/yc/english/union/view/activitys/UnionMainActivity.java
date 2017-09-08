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
import com.example.comm_recyclviewadapter.RecycleViewUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
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
import com.yc.english.union.view.adapter.GroupGroupAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

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

    private GroupGroupAdapter adapter;
    private List<ClassInfo> mClassInfo;

    private int page = 1;
    private RecycleViewUtils recycleViewUtils;
    private boolean isScorll = true;

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
        adapter = new GroupGroupAdapter(this, true, null);
        recyclerView.setAdapter(adapter);
        getData(false, true);
        initListener();
    }

    private void initListener() {
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private LinearLayoutManager layoutManager;
            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemCount = layoutManager.getItemCount();
                recycleViewUtils = new RecycleViewUtils(recyclerView);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == itemCount - 1 && isScorll &&
                        recycleViewUtils.isFullScreen()) {
                    getData(true, false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
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
                this.mClassInfo = classInfos;
                if (isFitst) {
                    llDataContainer.setVisibility(View.VISIBLE);
                    llEmptyContainer.setVisibility(View.GONE);
                }
                adapter.setData(classInfos, isScorll);
            } else {
                llDataContainer.setVisibility(View.GONE);
                llEmptyContainer.setVisibility(View.VISIBLE);
                hideStateView();
                if (ActivityUtils.isValidContext(this)) {
                    showCreateGuide();
                }
            }
        } else {
            isScorll = !(classInfos == null || classInfos.isEmpty());
            adapter.addData(classInfos, isScorll, recycleViewUtils.isFullScreen());
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


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.UNREAD_MESSAGE)
            }
    )
    public void getMessage(Message message) {

        if (message.getContent() instanceof RichContentMessage) {
            adapter.setMessage(message);
        }
        for (int i = 0; i < mClassInfo.size(); i++) {
            if (mClassInfo.get(i).getClass_id().equals(message.getTargetId())) {
                adapter.notifyItemRangeChanged(i, 1);
            }
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
            showUnionList(null,false,true);
        }
    }

}
