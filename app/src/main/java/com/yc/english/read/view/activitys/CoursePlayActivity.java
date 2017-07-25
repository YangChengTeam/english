package com.yc.english.read.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.read.JsonTools;
import com.yc.english.read.domain.EnglishCourse;
import com.yc.english.read.view.adapter.ItemClickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CoursePlayActivity extends BaseActivity {


    @BindView(R.id.layout_course_play)
    LinearLayout mCoursePlayLayout;

    @BindView(R.id.rv_course_list)
    RecyclerView mCourseRecyclerView;

    ItemClickAdapter mItemAdapter;

    List<EnglishCourse> datas;

    private int playPosition;

    @Override
    public int getLayoutID() {
        return R.layout.read_activity_course_play;
    }

    @Override
    public void init() {
        initData();
    }

    public void initData(){
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datas = JsonTools.jsonData(CoursePlayActivity.this, "english_course.json");
        mItemAdapter = new ItemClickAdapter(this, datas);
        mCourseRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(CoursePlayActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.layout_course_play)
    public void coursePlay() {
        LogUtils.e("coursePlay--->");
    }

}
