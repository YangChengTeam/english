package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.Constant;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;
import com.yc.english.read.model.domain.GradeInfoList;
import com.yc.english.read.model.engin.BookEngin;
import com.yc.english.read.presenter.AddBookPresenter;
import com.yc.english.read.view.adapter.CourseVersionItemClickAdapter;
import com.yc.english.read.view.adapter.GradeItemClickAdapter;

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

    private GradeItemClickAdapter mGradeAdapter;

    private List<GradeInfo> mGradeDatas;

    private CourseVersionItemClickAdapter mCourseVersionAdapter;

    private int viewType = 1;

    GridLayoutManager gradeGridLayoutManager;

    private int lastGradePosition = -1;

    private int lastVersionPosition = -1;

    BookEngin bookEngin;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_add_book;
    }

    @Override
    public void init() {

        bookEngin = new BookEngin(AddBookActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewType = bundle.getInt("view_type", 1);
        }

        mToolbar.setTitle(getString(R.string.read_study_grade_text));
        mToolbar.showNavigationIcon();

        gradeGridLayoutManager = new GridLayoutManager(this, 4);
        mGradeRecyclerView.setLayoutManager(gradeGridLayoutManager);
        mGradeAdapter = new GradeItemClickAdapter(this, null);
        mGradeRecyclerView.setAdapter(mGradeAdapter);

        mGradeBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mCourseVersionAdapter = new CourseVersionItemClickAdapter(this, null);
        mGradeBookRecyclerView.setAdapter(mCourseVersionAdapter);

        mPresenter = new AddBookPresenter(this, this);
        //获取年级集合
        mPresenter.gradeList();
        mPresenter.getCVListByGradeId(null, null);//获取所有的教材版本

        //选择年级
        mGradeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mGradeDatas != null) {

                    if (lastGradePosition > -1) {
                        ((GradeInfo) mGradeAdapter.getData().get(lastGradePosition)).setSelected(false);
                    }

                    ((GradeInfo) mGradeAdapter.getData().get(position)).setSelected(true);

                    if (lastGradePosition != position) {
                        lastGradePosition = position;
                        mGradeAdapter.notifyDataSetChanged();

                        //获取教材版本
                        String gradeId = ((GradeInfo) mGradeAdapter.getData().get(position)).getGrade();
                        String partType = ((GradeInfo) mGradeAdapter.getData().get(position)).getPartType();
                        mPresenter.getCVListByGradeId(gradeId, partType);
                    }
                }
            }
        });

        //选择教材版本
        mCourseVersionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (viewType == 1) {

                    if (!((CourseVersionInfo) adapter.getData().get(position)).isAdd()) {
                        return;
                    }

                    if (lastGradePosition == -1) {
                        TipsHelper.tips(AddBookActivity.this, "请先选择年级");
                        return;
                    }

                    if (mGradeDatas != null) {
                        if (lastVersionPosition > -1) {
                            ((CourseVersionInfo) mCourseVersionAdapter.getData().get(lastVersionPosition)).setSelected(false);
                        }
                        ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).setSelected(true);
                        if (lastVersionPosition != position) {
                            lastVersionPosition = position;
                        }
                        mCourseVersionAdapter.notifyDataSetChanged();

                        String bookId = ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).getBookId();
                        String bookImageUrl = ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).getBookImageUrl();
                        String gradeName = ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).getGradeName();
                        String versionName = ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).getVersionName();

                        BookInfo bookInfo = new BookInfo(BookInfo.CLICK_ITEM_VIEW);
                        bookInfo.setBookId(bookId);
                        bookInfo.setCoverImg(bookImageUrl);
                        bookInfo.setGradeName(gradeName);
                        bookInfo.setVersionName(versionName);

                        RxBus.get().post(Constant.ADD_BOOK_INFO, bookInfo);

                        Intent intent = new Intent(AddBookActivity.this, BookUnitActivity.class);
                        intent.putExtra("book_id", bookId);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    public void showGradeListData(GradeInfoList gInfo) {
        if (gInfo != null && gInfo.getList() != null) {
            mGradeDatas = gInfo.getList();
            mGradeAdapter.setNewData(mGradeDatas);
            mGradeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCVListData(List<CourseVersionInfo> list) {
        if (list != null) {
            mCourseVersionAdapter.setNewData(list);
        }
    }

}
