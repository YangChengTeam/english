package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.Constant;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;
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

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    LinearLayout mLayoutContext;

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


    @Override
    public int getLayoutId() {
        return R.layout.read_activity_add_book;
    }

    @Override
    public void init() {


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

        //mPresenter.getGradeListFromLocal();

        //获取年级集合
        mPresenter.gradeList();
        mPresenter.getCVListByGradeId(null, null);//获取所有的教材版本

        //选择年级
        mGradeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mGradeDatas != null) {

                if (lastGradePosition > -1) {
                    mGradeAdapter.getData().get(lastGradePosition).setSelected(false);
                }

                mGradeAdapter.getData().get(position).setSelected(true);

                if (lastGradePosition != position) {
                    lastGradePosition = position;
                    mGradeAdapter.notifyDataSetChanged();

                    //获取教材版本
                    String gradeId = mGradeAdapter.getData().get(position).getGrade();
                    String partType = mGradeAdapter.getData().get(position).getPartType();
                    mPresenter.getCVListByGradeId(gradeId, partType);
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
                            mCourseVersionAdapter.getData().get(lastVersionPosition).setSelected(false);
                        }
                        ((CourseVersionInfo) mCourseVersionAdapter.getData().get(position)).setSelected(true);
                        if (lastVersionPosition != position) {
                            lastVersionPosition = position;
                        }
                        mCourseVersionAdapter.notifyDataSetChanged();

                        CourseVersionInfo courseVersionInfo = (CourseVersionInfo) mCourseVersionAdapter.getData().get(position);
                        String bookId = courseVersionInfo.getBookId();
                        String bookImageUrl = courseVersionInfo.getBookImageUrl();
                        String gradeName = courseVersionInfo.getGradeName();
                        String versionName = courseVersionInfo.getVersionName();

                        BookInfo bookInfo = new BookInfo(BookInfo.CLICK_ITEM_VIEW);
                        bookInfo.setBookId(bookId);
                        bookInfo.setCoverImg(bookImageUrl);
                        bookInfo.setGradeName(gradeName);
                        bookInfo.setVersionName(versionName);

                        RxBus.get().post(Constant.ADD_BOOK_INFO, bookInfo);
                        Intent intent = null;
                        if (ReadApp.READ_COMMON_TYPE == 1) {
                            intent = new Intent(AddBookActivity.this, BookUnitActivity.class);
                        } else if (ReadApp.READ_COMMON_TYPE == 2) {
                            intent = new Intent(AddBookActivity.this, WordUnitActivity.class);
                        }
                        intent.putExtra("book_id", bookId);
                        startActivity(intent);
                        AddBookActivity.this.finish();
                    }

                }
            }
        });
    }

    @Override
    public void hide() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取年级集合
                mPresenter.gradeList();
                mPresenter.getCVListByGradeId(null, null);//获取所有的教材版本
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mLayoutContext);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mLayoutContext);
    }

    @Override
    public void showGradeListData(List<GradeInfo> gradeInfos) {
        if (gradeInfos != null) {
            mGradeDatas = gradeInfos;
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
