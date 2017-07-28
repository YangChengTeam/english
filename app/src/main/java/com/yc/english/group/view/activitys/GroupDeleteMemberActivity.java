package com.yc.english.group.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.listener.onCheckedChangeListener;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.view.adapter.GroupDeleteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/27 08:44.
 */

public class GroupDeleteMemberActivity extends FullScreenActivity implements BaseToolBar.OnItemClickLisener, onCheckedChangeListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_confirm_delete_group)
    TextView tvConfirmDeleteGroup;


    private List<GroupMemberInfo> memberInfoList = new ArrayList<>();
    private GroupDeleteAdapter adapter;

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.delete_member));
        mToolbar.setMenuTitle(getResources().getString(R.string.cancel));
        mToolbar.setMenuTitleColor(getResources().getColor(R.color.gray_aaa));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupDeleteAdapter(this, null);
        recyclerView.setAdapter(adapter);
        initData();
        initListener();

    }

    private void initListener() {
        adapter.setListener(this);
        mToolbar.setOnItemClickLisener(this);

    }

    private void initData() {
        memberInfoList.add(new GroupMemberInfo("刘老师", "", true));
        memberInfoList.add(new GroupMemberInfo("艾同学", "", false));
        memberInfoList.add(new GroupMemberInfo("曹同学", "", false));
        memberInfoList.add(new GroupMemberInfo("蔡同学", "", false));
        memberInfoList.add(new GroupMemberInfo("张同学", "", false));
        memberInfoList.add(new GroupMemberInfo("王同学", "", false));
        memberInfoList.add(new GroupMemberInfo("程同学", "", false));
        memberInfoList.add(new GroupMemberInfo("刘同学", "", false));
        adapter.setData(memberInfoList);

    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_delete_member;
    }

    private int count;//计数
    private List<CompoundButton> buttons = new ArrayList<>();

    @Override
    public void onCheckedChange(int position, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            count++;
            buttons.add(buttonView);
        } else {
            count--;
            buttons.remove(buttonView);
        }
        LogUtils.e(position + "---" + isChecked + "----" + count);

        tvConfirmDeleteGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        mToolbar.setMenuTitleColor(count > 0 ? getResources().getColor(R.color.primary) : getResources().getColor(R.color.gray_aaa));

        tvConfirmDeleteGroup.setText(String.format(getResources().getString(R.string.confirm_delete), count));
    }

    @Override
    public void onClick() {
        if (buttons.size() > 0) {

            for (Object o : buttons.toArray()) {
                ((CompoundButton) o).setChecked(false);
            }
            buttons.clear();
        } else {
            ToastUtils.showShort("你没有要取消的成员");
        }
    }

}
