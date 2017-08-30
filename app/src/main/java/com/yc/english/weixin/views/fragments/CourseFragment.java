package com.yc.english.weixin.views.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.WebActivity;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.views.adapters.CourseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseFragment extends BaseFragment {

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;

    private CourseAdapter mCourseAdapter;

    @Override
    public void init() {
        List<CourseInfo> courseInfos = new ArrayList<>();
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setTitle("每日单词");
        courseInfo.setUrl("http://www.baidu.com");
        courseInfos.add(courseInfo);


        CourseInfo courseInfo2 = new CourseInfo();
        courseInfo2.setTitle("每日单词");
        courseInfos.add(courseInfo2);

        CourseInfo courseInfo3 = new CourseInfo();
        courseInfo3.setTitle("每日单词");
        courseInfos.add(courseInfo3);

        CourseInfo courseInfo4 = new CourseInfo();
        courseInfo4.setTitle("每日单词");
        courseInfos.add(courseInfo4);

        CourseInfo courseInfo5 = new CourseInfo();
        courseInfo5.setTitle("每日单词");
        courseInfos.add(courseInfo5);

        CourseInfo courseInfo6 = new CourseInfo();
        courseInfo6.setTitle("每日单词");
        courseInfos.add(courseInfo6);

        CourseInfo courseInfo7 = new CourseInfo();
        courseInfo7.setTitle("每日单词");
        courseInfos.add(courseInfo7);

        CourseInfo courseInfo8 = new CourseInfo();
        courseInfo8.setTitle("每日单词");
        courseInfos.add(courseInfo8);

        CourseInfo courseInfo9 = new CourseInfo();
        courseInfo9.setTitle("每日单词");
        courseInfos.add(courseInfo9);

        CourseInfo courseInfo10 = new CourseInfo();
        courseInfo10.setTitle("每日单词");
        courseInfos.add(courseInfo10);

        CourseInfo courseInfo11 = new CourseInfo();
        courseInfo11.setTitle("每日单词");
        courseInfos.add(courseInfo11);

        CourseInfo courseInfo12 = new CourseInfo();
        courseInfo12.setTitle("每日单词");
        courseInfos.add(courseInfo12);

        CourseInfo courseInfo13 = new CourseInfo();
        courseInfo13.setTitle("每日单词");
        courseInfos.add(courseInfo13);

        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCourseAdapter = new CourseAdapter(courseInfos);
        mCourseRecyclerView.setAdapter(mCourseAdapter);

        mCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseInfo courseInfo = (CourseInfo) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", courseInfo.getTitle());
                intent.putExtra("url", courseInfo.getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course;
    }
}
