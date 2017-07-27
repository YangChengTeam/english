package com.yc.english.group.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.view.adapter.GroupDeleteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/27 08:44.
 */

public class GroupDeleteMemberActivity extends BaseActivity {
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private List<GroupMemberInfo> memberInfoList = new ArrayList<>();
    private GroupDeleteAdapter adapter;

    @Override
    public void init() {
        txt1.setText(getResources().getString(R.string.delete_member));
        img3.setVisibility(View.GONE);
        tvManager.setVisibility(View.VISIBLE);
        tvManager.setText(getResources().getString(R.string.cancel));
        tvManager.setTextColor(getResources().getColor(R.color.group_gray_aaa));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupDeleteAdapter(this, null);
        recyclerView.setAdapter(adapter);
        initData();

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


}
