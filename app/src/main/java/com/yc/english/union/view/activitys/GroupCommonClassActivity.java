package com.yc.english.union.view.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupCommonClassContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupCommonClassPresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.view.activitys.student.UnionJoinActivity;
import com.yc.english.union.view.activitys.teacher.GroupVerifyActivity;
import com.yc.english.union.view.adapter.GroupGroupAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/1 18:16.
 */

public class GroupCommonClassActivity extends FullScreenActivity<GroupCommonClassPresenter> implements GroupCommonClassContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private GroupGroupAdapter adapter;
    private ClassInfo mClassInfo;

    @Override
    public void init() {
        mPresenter = new GroupCommonClassPresenter(this, this);
        mToolbar.setTitle(getString(R.string.teacher_education));
        mToolbar.showNavigationIcon();

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupCommonClassActivity.this, GroupVerifyActivity.class);
                intent.putExtra("flag", "comm");
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupGroupAdapter(this, false, null);
        recyclerView.setAdapter(adapter);
        initListener();

    }

    private void initListener() {
        RxView.clicks(btnJoinClass).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(GroupCommonClassActivity.this))
                    startActivity(new Intent(GroupCommonClassActivity.this, UnionJoinActivity.class));
            }
        });

        adapter.setOnJoinListener(new GroupGroupAdapter.OnJoinListener() {
            @Override
            public void onJoin(ClassInfo classInfo) {
                GroupCommonClassActivity.this.mClassInfo = classInfo;
                int result = SPUtils.getInstance().getInt(classInfo.getClass_id() + "member");
                if (!UserInfoHelper.isGotoLogin(GroupCommonClassActivity.this)) {
                    if (result == 1) {
                        setMode(classInfo);
                        RongIM.getInstance().startGroupChat(GroupCommonClassActivity.this, classInfo.getClass_id(), classInfo.getClassName());
                    } else {
                        mPresenter.isGroupMember(classInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid());
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_list_join;
    }


    @Override
    public void showCommonClassList(List<ClassInfo> list) {
        adapter.setData(list);
    }

    @Override
    public void showIsMember(int is_member) {

        SPUtils.getInstance().put(mClassInfo.getClass_id() + "member", is_member);

        if (is_member == 1) {//已经是班群成员
            setMode(mClassInfo);
            RongIM.getInstance().startGroupChat(this, mClassInfo.getClass_id(), mClassInfo.getClassName());
        } else {
            final AlertDialog dialog = new AlertDialog(this);
            dialog.setDesc("是否申请加入该班群?");
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.applyJoinGroup(UserInfoHelper.getUserInfo().getUid(), mClassInfo.getGroupId() + "");
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void showVerifyResult(List<StudentInfo> list) {
        if (list != null && list.size() > 0) {
            mToolbar.setMenuIcon(R.mipmap.group65);
        } else {
            mToolbar.setMenuIcon(R.mipmap.group66);
        }
        invalidateOptionsMenu();
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
                mPresenter.loadData(true);
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

    private void setMode(ClassInfo classInfo) {
        if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
            GroupApp.setMyExtensionModule(true);
        } else {
            GroupApp.setMyExtensionModule(false);
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
}
