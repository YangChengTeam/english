package com.yc.english.group.view.fragments;

import android.view.View;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.MainToolBar;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class ClassMainFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    MainToolBar toolbar;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;

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

                break;
            case R.id.btn_join_class:
                break;

        }

    }
}
