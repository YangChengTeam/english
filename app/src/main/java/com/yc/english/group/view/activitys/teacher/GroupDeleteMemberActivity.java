package com.yc.english.group.view.activitys.teacher;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupDeleteMemberContract;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupDeleteMemberPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.adapter.GroupDeleteAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 08:44.
 */

public class GroupDeleteMemberActivity extends FullScreenActivity<GroupDeleteMemberPresenter> implements BaseToolBar.OnItemClickLisener, OnCheckedChangeListener<StudentInfo>, GroupDeleteMemberContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_confirm_delete_group)
    TextView tvConfirmDeleteGroup;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private GroupDeleteAdapter adapter;
    private GroupInfo groupInfo;
    private List<StudentInfo> mList;
    private AlertDialog alertDialog;

    @Override
    public void init() {
        mPresenter = new GroupDeleteMemberPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.delete_member));
        mToolbar.setMenuTitle(getResources().getString(R.string.cancel));

        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
        }

        mToolbar.setMenuTitleColor(getResources().getColor(R.color.gray_aaa));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupDeleteAdapter(this, null);
        recyclerView.setAdapter(adapter);
        mPresenter.getMemberList(this, groupInfo.getId(), "1", "");
        initListener();

    }

    private void initListener() {
        adapter.setListener(this);
        mToolbar.setOnItemClickLisener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_delete_member;
    }

    private int count;//计数
    private List<ImageView> imageViews = new ArrayList<>();

    private List<StudentInfo> studentInfos = new ArrayList<>();


    @Override
    public void onClick(int position, View view, boolean isChecked, StudentInfo studentInfo) {

        if (view instanceof ImageView) {
            if (isChecked) {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                count++;
                imageViews.add(((ImageView) view));
                studentInfos.add(studentInfo);
            } else {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                count--;
                imageViews.remove(view);
                studentInfos.remove(studentInfo);
            }
        }

        LogUtils.e(position + "---" + isChecked + "----" + count + "---" + studentInfos.size());

        tvConfirmDeleteGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        mToolbar.setMenuTitleColor(count > 0 ? getResources().getColor(R.color.primary) : getResources().getColor(R.color.gray_aaa));

        tvConfirmDeleteGroup.setText(String.format(getResources().getString(R.string.confirm_delete), count));
        RxView.clicks(tvConfirmDeleteGroup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                final String[] userIds = new String[studentInfos.size()];
                for (int i = 0; i < studentInfos.size(); i++) {
                    String user_id = studentInfos.get(i).getUser_id();
                    userIds[i] = user_id;
                }
                if (alertDialog == null) {
                    alertDialog = new AlertDialog(GroupDeleteMemberActivity.this);
                }
                alertDialog.setDesc("是否删除学生");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mPresenter.deleteMember(groupInfo.getId(), UserInfoHelper.getUserInfo().getUid(), userIds);
                    }
                });
                alertDialog.show();


            }
        });

    }

    @Override
    public void onClick() {
        if (imageViews.size() > 0) {
            for (Object o : imageViews.toArray()) {
                ((ImageView) o).setImageDrawable(getResources().getDrawable(R.mipmap.group23));
            }
            imageViews.clear();
            adapter.notifyDataSetChanged();
            setDelete();

        } else {
            ToastUtils.showShort("你没有要取消的成员");
        }
    }


    @Override
    public void showMemberList(List<StudentInfo> list) {
        this.mList = list;
        adapter.setData(list);
    }

    //成员移除成功
    @Override
    public void showDeleteResult() {
        mList.removeAll(studentInfos);
        adapter.setData(mList);
        setDelete();
    }

    private void setDelete() {
        tvConfirmDeleteGroup.setVisibility(View.GONE);
        mToolbar.setMenuTitleColor(getResources().getColor(R.color.gray_aaa));
        studentInfos.clear();
        count = 0;
        RxBus.get().post(BusAction.GROUPLIST, "delete");
        RxBus.get().post(BusAction.DELETEMEMBER, "delete_member");
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
                mPresenter.getMemberList(GroupDeleteMemberActivity.this, groupInfo.getId(), "1", "");
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

}
