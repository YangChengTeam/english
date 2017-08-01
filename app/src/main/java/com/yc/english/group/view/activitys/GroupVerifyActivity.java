package com.yc.english.group.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.view.adapter.GroupVerifyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/29 11:58.
 */

public class GroupVerifyActivity extends FullScreenActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private List<GroupMemberInfo> list = new ArrayList<>();
    private GroupVerifyAdapter adapter;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.friend_verify));
        mToolbar.showNavigationIcon();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupVerifyAdapter(this, null);
        recyclerView.setAdapter(adapter);
        initData();
    }

    private void initData() {

        list.add(new GroupMemberInfo("蔡同学", "", "江夏区第一小学"));
        list.add(new GroupMemberInfo("李同学", "", "洪山区第一小学"));
        list.add(new GroupMemberInfo("王同学", "", "蔡甸区第一小学"));
        adapter.setData(list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_friend;
    }


}
