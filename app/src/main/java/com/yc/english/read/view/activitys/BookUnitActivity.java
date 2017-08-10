package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.contract.BookUnitContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.UnitInfo;
import com.yc.english.read.model.domain.UnitInfoList;
import com.yc.english.read.presenter.BookUnitPresenter;
import com.yc.english.read.view.adapter.ReadBookUnitItemClickAdapter;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class BookUnitActivity extends FullScreenActivity<BookUnitPresenter> implements BookUnitContract.View {

    @BindView(R.id.iv_book_grade)
    ImageView mBookGradeImageView;

    @BindView(R.id.tv_book_grade_name)
    TextView mBookGradeNameTextView;

    @BindView(R.id.tv_book_press)
    TextView mBookPressTextView;

    @BindView(R.id.btn_book_unit_total)
    Button mBookUnitTotalButton;

    @BindView(R.id.rv_book_unit_list)
    RecyclerView mBookUnitRecyclerView;

    ReadBookUnitItemClickAdapter mItemAdapter;

    private List<UnitInfo> mBookUnitDatas;

    private String bookId;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_book_unit;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookId = bundle.getString("book_id");
        }

        mToolbar.setTitle(getString(R.string.read_book_unit_text));
        mToolbar.showNavigationIcon();

        mPresenter = new BookUnitPresenter(this, this);

        mBookUnitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new ReadBookUnitItemClickAdapter(this, mBookUnitDatas);
        mBookUnitRecyclerView.setAdapter(mItemAdapter);
        mBookUnitRecyclerView.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(10)));
        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("position --->" + position);
                Intent intent = new Intent(BookUnitActivity.this, CoursePlayActivity.class);
                intent.putExtra("unit_id", ((UnitInfo) mItemAdapter.getData().get(position)).getId());
                startActivity(intent);
            }
        });

        mPresenter.getBookInfoById(bookId);
    }

    /**
     * 测试数据
     */
    public void initData() {
        mBookUnitDatas = new ArrayList<UnitInfo>();
        for (int i = 0; i < 6; i++) {
            UnitInfo unitInfo = new UnitInfo(UnitInfo.CLICK_ITEM_VIEW);
            unitInfo.setName("Unit 1 Hello,Good Morning");
            unitInfo.setWordCount((i + 1) * 2 + "句");
            mBookUnitDatas.add(unitInfo);
        }
    }

    @Override
    public void showBookUnitListData(UnitInfoList unitInfoList) {
        if (unitInfoList != null) {
            if (unitInfoList.getList() != null) {
                mBookUnitDatas = unitInfoList.getList();
                mItemAdapter.setNewData(mBookUnitDatas);
                mItemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showBookInfo(BookInfo bookInfo) {
        if (bookInfo != null) {
            Glide.with(BookUnitActivity.this).load(bookInfo.getCoverImg()).into(mBookGradeImageView);
            mBookGradeNameTextView.setText(bookInfo.getName());
            mBookPressTextView.setText(bookInfo.getPress());
            mBookUnitTotalButton.setText(bookInfo.getSentenceCount() + getString(R.string.read_sentence_text));
        }
    }
}
