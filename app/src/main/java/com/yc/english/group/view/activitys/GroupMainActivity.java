package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;
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
import com.yc.english.group.contract.GroupMyGroupListContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupMyGroupListPresenter;
import com.yc.english.group.view.activitys.student.GroupJoinActivity;
import com.yc.english.group.view.activitys.teacher.GroupCreateActivity;
import com.yc.english.group.view.activitys.teacher.GroupVerifyActivity;
import com.yc.english.group.view.adapter.GroupGroupAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class GroupMainActivity extends FullScreenActivity<GroupMyGroupListPresenter> implements GroupMyGroupListContract.View {
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

    private GroupGroupAdapter adapter;
    private List<ClassInfo> mClassInfo;

    @Override
    public void init() {

        mPresenter = new GroupMyGroupListPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group));
        mToolbar.showNavigationIcon();
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(GroupMainActivity.this, GroupVerifyActivity.class));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupGroupAdapter(this, true, null);
        recyclerView.setAdapter(adapter);

    }

    private void showCreateGuide() {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        final GuidePopupWindow guidePopupWindow = builder.setDelay(1f).setTargetView(btnCreateClass).setCorner(5).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {

                startActivity(new Intent(GroupMainActivity.this, GroupCreateActivity.class));
            }
        })
                .build(this);
        guidePopupWindow.addCustomView(R.layout.guide_create_view, R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinGuide();
                guidePopupWindow.dismiss();
            }
        });

        guidePopupWindow.show(rootView, "create");
    }

    private void showJoinGuide() {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        final GuidePopupWindow guidePopupWindow = builder.setDelay(0).setTargetView(btnJoinClass).setCorner(5).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {
                startActivity(new Intent(GroupMainActivity.this, GroupJoinActivity.class));
            }
        })
                .build(this);
        guidePopupWindow.addCustomView(R.layout.group_guide_join, R.id.m_btn_OK, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidePopupWindow.dismiss();
            }
        });
        guidePopupWindow.show(rootView, "join");
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
                goToActivity(GroupCreateActivity.class);

                break;
            case R.id.btn_join_class:
            case R.id.btn_join_class1:
                goToActivity(GroupJoinActivity.class);
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
        mPresenter.loadData(true);
    }


    @Override
    public void showMyGroupList(List<ClassInfo> classInfos) {
        if (classInfos != null && classInfos.size() > 0) {
            this.mClassInfo = classInfos;
            llDataContainer.setVisibility(View.VISIBLE);
            llEmptyContainer.setVisibility(View.GONE);
            adapter.setData(classInfos);
        } else {
            llDataContainer.setVisibility(View.GONE);
            llEmptyContainer.setVisibility(View.VISIBLE);
            hideStateView();
            if (ActivityUtils.isValidContext(this)) {
                showCreateGuide();
            }
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
                mPresenter.loadData(true);
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

}
