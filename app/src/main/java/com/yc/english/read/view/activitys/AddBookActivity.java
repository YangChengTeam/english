package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.CommonInfo;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;
import com.yc.english.read.presenter.AddBookPresenter;
import com.yc.english.read.view.adapter.CourseVersionItemClickAdapter;
import com.yc.english.read.view.adapter.GradeItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/26.
 */

public class AddBookActivity extends FullScreenActivity<AddBookPresenter> implements AddBookContract.View {

    @BindView(R.id.rv_grade_list)
    RecyclerView mGradeRecyclerView;

    @BindView(R.id.rv_grade_book_list)
    RecyclerView mGradeBookRecyclerView;

    private String[] mReadGradeListValues;

    private GradeItemClickAdapter mGradeAdapter;

    private List<GradeInfo> mGradeDatas;

    private String[] mReadGradeBookListValues;

    private CourseVersionItemClickAdapter mCourseVersionAdapter;

    private List<CourseVersionInfo> mGradeBookDatas;

    private int viewType = 1;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_add_book;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewType = bundle.getInt("view_type",1);
        }

        mToolbar.setTitle(getString(R.string.read_study_grade_text));
        mToolbar.showNavigationIcon();

        mReadGradeListValues = getResources().getStringArray(R.array.read_grade_list_text);
        mReadGradeBookListValues = getResources().getStringArray(R.array.read_grade_book_version_text);
        mGradeDatas = new ArrayList<GradeInfo>();
        mGradeBookDatas = new ArrayList<CourseVersionInfo>();

        for (int i = 0; i < mReadGradeListValues.length; i++) {
            GradeInfo gradeInfo = new GradeInfo(CommonInfo.CLICK_ITEM_VIEW);
            gradeInfo.setGradeName(mReadGradeListValues[i]);
            mGradeDatas.add(gradeInfo);
        }

        for (int i = 0; i < mReadGradeBookListValues.length; i++) {
            CourseVersionInfo courseVersionInfo = new CourseVersionInfo(CommonInfo.CLICK_ITEM_VIEW);
            courseVersionInfo.setCourseVersionName(mReadGradeBookListValues[i]);
            mGradeBookDatas.add(courseVersionInfo);
        }

        mGradeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mGradeAdapter = new GradeItemClickAdapter(this, mGradeDatas);
        mGradeRecyclerView.setAdapter(mGradeAdapter);

        mGradeBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mCourseVersionAdapter = new CourseVersionItemClickAdapter(this, mGradeBookDatas);
        mGradeBookRecyclerView.setAdapter(mCourseVersionAdapter);

        mPresenter = new AddBookPresenter(this, this);

        mPresenter.gradeList();

        mGradeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String gradeId = mGradeDatas.get(position).getGradeId();
                mPresenter.getCVListByGradeId(gradeId);
            }
        });

        mCourseVersionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(viewType == 1){
                    Intent intent = new Intent(AddBookActivity.this, BookUnitActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void showGradeListData(ArrayList<GradeInfo> list) {
        if (list != null) {
            mGradeDatas = list;
            mGradeAdapter.setNewData(list);
            mGradeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCVListData(ArrayList<CourseVersionInfo> list) {
        if (list != null) {
            mCourseVersionAdapter.setNewData(list);
            mCourseVersionAdapter.notifyDataSetChanged();
        }
    }
}
