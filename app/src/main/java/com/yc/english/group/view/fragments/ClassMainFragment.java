package com.yc.english.group.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.MainToolBar;
import com.yc.english.base.view.ToolbarFragment;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.view.activitys.GroupCreateActivity;
import com.yc.english.group.view.activitys.GroupJoinActivity;
import com.yc.english.group.view.adapter.GroupGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class ClassMainFragment extends ToolbarFragment {
    private static final String TAG = "ClassMainFragment";

    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private List<ClassInfo> mlist = new ArrayList<>();
    private GroupGroupAdapter adapter;


    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.group));

        mToolbar.setMenuIcon(R.mipmap.group57);
        mToolbar.setMenuTitle("");


        if (mlist != null) {

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            adapter = new GroupGroupAdapter(getContext(), null);
            recyclerView.setAdapter(adapter);
            initData();

        } else {

        }

    }

    private void initData() {

        mlist.add(new ClassInfo("", "武汉街道口小学5年级一班", 20, 456666));
        mlist.add(new ClassInfo("", "武汉江夏区小学2年级三班", 15, 457777));
        mlist.add(new ClassInfo("", "武汉南湖小学3年级五班", 25, 458888));
        mlist.add(new ClassInfo("", "武汉洪山区小学6年级四班", 10, 45999));
        adapter.setData(mlist);

    }

    @Override
    public int getLayoutId() {
        if (mlist == null) {
            return R.layout.group_fragment_class_start;
        } else {
            return R.layout.group_fragment_class_list;
        }
    }

    @OnClick({R.id.btn_create_class, R.id.btn_join_class})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_class:
                startActivity(new Intent(getActivity(), GroupCreateActivity.class));
                break;
            case R.id.btn_join_class:
                startActivity(new Intent(getActivity(), GroupJoinActivity.class));
                break;

        }

    }


}
