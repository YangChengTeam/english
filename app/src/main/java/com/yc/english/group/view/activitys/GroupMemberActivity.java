package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.adapter.GroupMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/26 14:41.
 */

public class GroupMemberActivity extends BaseActivity {
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_share_group)
    TextView tvShareGroup;

    private List<GroupMemberInfo> mList = new ArrayList<>();
    private GroupMemberAdapter adapter;
    private GroupInfo groupInfo;


    @Override
    public void init() {
        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            txt1.setText(groupInfo.getName());
        }
        tvManager.setVisibility(View.VISIBLE);
        img3.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupMemberAdapter(this, null);
        recyclerView.setAdapter(adapter);
        initData();
        initListener();

    }

    private void initData() {
        mList.add(new GroupMemberInfo("刘老师", "", true));
        mList.add(new GroupMemberInfo("艾同学", "", false));
        mList.add(new GroupMemberInfo("曹同学", "", false));
        mList.add(new GroupMemberInfo("蔡同学", "", false));
        mList.add(new GroupMemberInfo("程同学", "", false));
        mList.add(new GroupMemberInfo("陈同学", "", false));
        mList.add(new GroupMemberInfo("王同学", "", false));
        adapter.setData(mList);

    }

    private void initListener() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_class_manager;
    }

    @OnClick({R.id.img1, R.id.tv_manager, R.id.tv_share_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
                finish();
                break;
            case R.id.tv_manager:
                Intent intent = new Intent(this, GroupManagerActivity.class);
                intent.putExtra("group", groupInfo);
                startActivity(intent);
                break;
            case R.id.tv_share_group:
                break;
        }
    }

}
