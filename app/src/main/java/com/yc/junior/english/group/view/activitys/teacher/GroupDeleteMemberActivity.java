package com.yc.junior.english.group.view.activitys.teacher;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.AlertDialog;
import com.yc.junior.english.base.view.BaseToolBar;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.contract.GroupDeleteMemberContract;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.presenter.GroupDeleteMemberPresenter;
import com.yc.junior.english.group.view.adapter.GroupDeleteAdapter;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 08:44.
 */

public class GroupDeleteMemberActivity extends FullScreenActivity<GroupDeleteMemberPresenter> implements GroupDeleteMemberContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_confirm_delete_group)
    TextView tvConfirmDeleteGroup;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private GroupDeleteAdapter adapter;
    private List<StudentInfo> mList;
    private AlertDialog alertDialog;

    private String name = "学生";

    @Override
    public void init() {
        mPresenter = new GroupDeleteMemberPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.delete_member));
        mToolbar.setMenuTitle(getResources().getString(R.string.cancel));

        mToolbar.setMenuTitleColor(ContextCompat.getColor(this, R.color.gray_aaa));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupDeleteAdapter(null);
        recyclerView.setAdapter(adapter);

        if (GroupInfoHelper.getClassInfo().getType().equals("1")) {
            name = "会员";
        }
        initListener();
    }

    private void initListener() {
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                ImageView iv = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_delete_select);

                StudentInfo studentInfo = (StudentInfo) adapter.getItem(position);
                iv.setTag(!(Boolean) iv.getTag());
                if (position != 0) {
                    if (((Boolean) iv.getTag())) {
                        iv.setImageDrawable(ContextCompat.getDrawable(GroupDeleteMemberActivity.this, R.mipmap.group24));
                        count++;
                        imageViews.add(iv);
                        studentInfos.add(studentInfo);
                        studentInfo.setIsSelected(true);
                    } else {
                        iv.setImageDrawable(ContextCompat.getDrawable(GroupDeleteMemberActivity.this, R.mipmap.group23));
                        count--;
                        imageViews.remove(iv);
                        studentInfos.remove(studentInfo);
                        studentInfo.setIsSelected(false);
                    }
                }

                LogUtils.e("position: " + position + "--" + iv.getTag() + "--" + count + "---" + studentInfos.size());


                tvConfirmDeleteGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                mToolbar.setMenuTitleColor(count > 0 ?
                        ContextCompat.getColor(GroupDeleteMemberActivity.this, R.color.primary) :
                        ContextCompat.getColor(GroupDeleteMemberActivity.this, R.color.gray_aaa));

                tvConfirmDeleteGroup.setText(String.format(getResources().getString(R.string.confirm_delete), count));
                RxView.clicks(tvConfirmDeleteGroup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        final StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < studentInfos.size(); i++) {
                            String user_id = studentInfos.get(i).getUser_id();
                            sb.append(user_id).append(",");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        if (alertDialog == null) {
                            alertDialog = new AlertDialog(GroupDeleteMemberActivity.this);
                        }
                        alertDialog.setDesc("是否删除" + name);
                        alertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                                mPresenter.deleteMember(GroupInfoHelper.getGroupInfo().getId(), UserInfoHelper.getUserInfo().getUid(), sb.toString());
                            }
                        });
                        alertDialog.show();


                    }
                });
            }
        });
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (imageViews.size() == 0) {
                    ToastUtils.showShort("你没有要取消的成员");
                } else {
                    clearData();
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_delete_member;
    }

    private int count;//计数
    private List<ImageView> imageViews = new ArrayList<>();

    private List<StudentInfo> studentInfos = new ArrayList<>();


    @Override
    public void showMemberList(List<StudentInfo> list) {
        this.mList = list;
        adapter.setNewData(list);

    }

    //成员移除成功
    @Override
    public void showDeleteResult() {
        mList.removeAll(studentInfos);
        adapter.setNewData(mList);
        setDelete();
    }


    private void clearData() {
        if (imageViews.size() > 0) {
            for (Object o : imageViews.toArray()) {
                ((ImageView) o).setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.group23));
            }
            imageViews.clear();
            adapter.notifyDataSetChanged();
        }
        if (studentInfos.size() > 0) {
            for (Object o : studentInfos.toArray()) {
                ((StudentInfo) o).setIsSelected(false);
            }
            studentInfos.clear();
        }
        tvConfirmDeleteGroup.setVisibility(View.GONE);
        mToolbar.setMenuTitleColor(ContextCompat.getColor(this, R.color.gray_aaa));
        count = 0;
    }

    private void setDelete() {
        clearData();
        RxBus.get().post(BusAction.GROUP_LIST, "delete");
        RxBus.get().post(BusAction.DELETE_MEMBER, "delete_member");
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

}
