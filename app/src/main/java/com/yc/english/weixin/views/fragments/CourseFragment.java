package com.yc.english.weixin.views.fragments;

import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseFragment extends BaseFragment {

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;

    @Override
    public void init() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course;
    }
}
