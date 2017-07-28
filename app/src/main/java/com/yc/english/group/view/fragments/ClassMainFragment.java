package com.yc.english.group.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.MainToolBar;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.view.activitys.GroupCreateActivity;
import com.yc.english.group.view.activitys.GroupJoinActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class ClassMainFragment extends BaseFragment {
    private static final String TAG = "ClassMainFragment";
    @BindView(R.id.toolbar)
    MainToolBar toolbar;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;

    private List<ClassInfo> mlist;


    @Override
    public void init() {
        toolbar.setTitle(getString(R.string.group));


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
