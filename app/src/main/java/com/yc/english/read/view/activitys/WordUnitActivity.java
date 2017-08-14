package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.contract.WordUnitContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.WordUnitInfo;
import com.yc.english.read.model.domain.WordUnitInfoList;
import com.yc.english.read.presenter.WordUnitPresenter;
import com.yc.english.read.view.adapter.ReadWordUnitItemClickAdapter;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/26.
 */

public class WordUnitActivity extends FullScreenActivity<WordUnitPresenter> implements WordUnitContract.View {

    @BindView(R.id.iv_book_top)
    ImageView mBookGradeImageView;

    @BindView(R.id.tv_book_grade_name)
    TextView mBookGradeNameTextView;

    @BindView(R.id.tv_book_press)
    TextView mBookPressTextView;

    @BindView(R.id.rv_word_unit_list)
    RecyclerView mWordUnitRecyclerView;

    ReadWordUnitItemClickAdapter mItemAdapter;

    private String bookId;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_unit;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bookId = bundle.getString("book_id");
        }

        mPresenter = new WordUnitPresenter(this, this);

        mToolbar.setTitle(getString(R.string.read_book_unit_text));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.word_book_change_text));
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(WordUnitActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        mWordUnitRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mItemAdapter = new ReadWordUnitItemClickAdapter(this, null);
        mWordUnitRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (ReadApp.READ_COMMON_TYPE == 2) {
                    Intent intent = new Intent(WordUnitActivity.this, ReadWordActivity.class);
                    intent.putExtra("unit_id", ((WordUnitInfo) mItemAdapter.getData().get(position)).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WordUnitActivity.this, WordPracticeActivity.class);
                    startActivity(intent);
                }
            }
        });

        mPresenter.getBookInfoById(bookId);
    }

    @Override
    public void showWordUnitListData(WordUnitInfoList wordUnitInfoList) {
        if (wordUnitInfoList != null) {
            mItemAdapter.setNewData(wordUnitInfoList.list);
        }
    }

    @Override
    public void showBookInfo(BookInfo bookInfo) {
        if (bookInfo != null) {
            Glide.with(WordUnitActivity.this).load(bookInfo.getCoverImg()).into(mBookGradeImageView);
            mBookGradeNameTextView.setText(bookInfo.getName());
            mBookPressTextView.setText(bookInfo.getPress());
        }
    }
}
