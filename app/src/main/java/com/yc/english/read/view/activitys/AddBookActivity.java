package com.yc.english.read.view.activitys;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.CommonInfo;
import com.yc.english.read.view.adapter.ReadAddBookItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/26.
 */

public class AddBookActivity extends FullScreenActivity {

    @BindView(R.id.rv_grade_list)
    RecyclerView mGradeRecyclerView;

    @BindView(R.id.rv_grade_book_list)
    RecyclerView mGradeBookRecyclerView;

    private String[] mReadGradeListValues;

    private ReadAddBookItemClickAdapter mGradeAdapter;

    private List<CommonInfo> mGradeDatas;

    private String[] mReadGradeBookListValues;

    private ReadAddBookItemClickAdapter mGradeBookAdapter;

    private List<CommonInfo> mGradeBookDatas;

    @Override
    public int getLayoutID() {
        return R.layout.read_activity_add_book;
    }

    @Override
    public void init() {

        mToolbar.setTitle(getString(R.string.read_study_grade_text));
        mToolbar.showNavigationIcon();

        mReadGradeListValues = getResources().getStringArray(R.array.read_grade_list_text);
        mReadGradeBookListValues = getResources().getStringArray(R.array.read_grade_book_version_text);
        mGradeDatas = new ArrayList<CommonInfo>();
        mGradeBookDatas = new ArrayList<CommonInfo>();

        for (int i = 0; i < mReadGradeListValues.length; i++) {
            CommonInfo commonEntity = new CommonInfo(CommonInfo.CLICK_ITEM_VIEW);
            commonEntity.setCommonName(mReadGradeListValues[i]);
            mGradeDatas.add(commonEntity);
        }

        for (int i = 0; i < mReadGradeBookListValues.length; i++) {
            CommonInfo commonEntity = new CommonInfo(CommonInfo.CLICK_ITEM_VIEW);
            commonEntity.setCommonName(mReadGradeBookListValues[i]);
            mGradeBookDatas.add(commonEntity);
        }

        mGradeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mGradeAdapter = new ReadAddBookItemClickAdapter(this, mGradeDatas);
        mGradeRecyclerView.setAdapter(mGradeAdapter);

        mGradeBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mGradeBookAdapter = new ReadAddBookItemClickAdapter(this, mGradeBookDatas);
        mGradeBookRecyclerView.setAdapter(mGradeBookAdapter);
    }
}
