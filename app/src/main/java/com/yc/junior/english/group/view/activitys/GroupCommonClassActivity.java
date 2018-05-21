package com.yc.junior.english.group.view.activitys;

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
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.AlertDialog;
import com.yc.junior.english.base.view.BaseToolBar;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.group.common.GroupApp;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.model.bean.ClassInfo;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.view.activitys.student.GroupJoinActivity;
import com.yc.junior.english.group.view.activitys.teacher.GroupVerifyActivity;
import com.yc.junior.english.group.view.adapter.GroupGroupAdapter;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.union.contract.UnionCommonListContract;
import com.yc.junior.english.union.presenter.UnionCommonListPresenter;
import com.yc.junior.english.vip.utils.VipDialogHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/1 18:16.
 */

public class GroupCommonClassActivity extends FullScreenActivity<UnionCommonListPresenter> implements UnionCommonListContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private GroupGroupAdapter adapter;


    @Override
    public void init() {
        mPresenter = new UnionCommonListPresenter(this, this);
        mToolbar.setTitle(getString(R.string.teacher_education));
        mToolbar.showNavigationIcon();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupGroupAdapter(this, false, null);
        recyclerView.setAdapter(adapter);
        initListener();
        getData();
    }

    private void initListener() {
        RxView.clicks(btnJoinClass).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(GroupCommonClassActivity.this))
                    if (UserInfoHelper.getUserInfo().getIsVip() == 1 || UserInfoHelper.getUserInfo().getIsVip() == 2) {
                        startActivity(new Intent(GroupCommonClassActivity.this, GroupJoinActivity.class));
                    }else {
                        VipDialogHelper.showVipDialog(getSupportFragmentManager(), null, null);
                        MobclickAgent.onEvent(GroupCommonClassActivity.this, "top_teacher_tutorship", "名师辅导");
                    }
            }
        });

        adapter.setOnJoinListener(new GroupGroupAdapter.OnJoinListener() {
            @Override
            public void onJoin(ClassInfo classInfo) {

                if (!UserInfoHelper.isGotoLogin(GroupCommonClassActivity.this)) {
                    if (UserInfoHelper.getUserInfo().getIsVip() == 1 || UserInfoHelper.getUserInfo().getIsVip() == 2) {
                        int result = SPUtils.getInstance().getInt(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid());
                        if (result == 1) {
                            setMode(classInfo);
                        } else {
                            mPresenter.isGroupMember(classInfo);
                        }
                    } else {
                        VipDialogHelper.showVipDialog(getSupportFragmentManager(), null, null);
                        MobclickAgent.onEvent(GroupCommonClassActivity.this, "top_teacher_tutorship", "名师辅导");
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
    public void showUnionList(List<ClassInfo> data, int page, boolean isFitst) {

    }

    @Override
    public void showMemberList(final List<StudentInfo> list) {
        if (list != null && list.size() > 0) {
            mToolbar.setMenuIcon(R.mipmap.group65);
        } else {
            mToolbar.setMenuIcon(R.mipmap.group66);
        }
        invalidateOptionsMenu();

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupCommonClassActivity.this, GroupVerifyActivity.class);
                intent.putExtra("type", "2");
                intent.putParcelableArrayListExtra("studentList", (ArrayList<StudentInfo>) list);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showMyGroupList(List<ClassInfo> list) {

    }

    @Override
    public void showCommonClassList(List<ClassInfo> list) {
        adapter.setData(list);
    }

    @Override
    public void showIsMember(int is_member, final ClassInfo classInfo) {

        SPUtils.getInstance().put(classInfo.getClass_id() + UserInfoHelper.getUserInfo().getUid(), is_member);

        if (is_member == 1) {//已经是班群成员
            setMode(classInfo);
        } else {
            final AlertDialog dialog = new AlertDialog(this);
            dialog.setDesc("是否申请加入该班群?");
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

    private void setMode(ClassInfo classInfo) {

        GroupApp.setMyExtensionModule(false, true);

        RongIM.getInstance().startGroupChat(this, classInfo.getClass_id(), classInfo.getClassName());
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


    private void getData() {
        mPresenter.getCommonClassList();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            String uid = userInfo.getUid();
            mPresenter.getMemberList("", "0", 1, 10, uid, "2");
        }
    }
}
