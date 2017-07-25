package com.yc.english.group.view.fragments;

import android.content.Intent;
import android.view.View;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.view.activitys.GroupCreateActivity;
import com.yc.english.group.view.activitys.GroupJoinActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class ClassMainFragment extends BaseFragment {
    private static final String TAG = "ClassMainFragment";
    private List<ClassInfo> mlist;


    @Override
    public void init() {

    }

    @Override
    public int getLayoutID() {
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
                startActivity(new Intent(getActivity(),GroupJoinActivity.class));
                break;

        }

    }
}
