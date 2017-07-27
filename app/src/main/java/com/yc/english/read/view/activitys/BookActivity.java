package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.view.adapter.ReadBookItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/25.
 */

public class BookActivity extends FullScreenActivity {

    @BindView(R.id.rv_book_list)
    RecyclerView mBookRecyclerView;

    @BindView(R.id.btn_edit_books)
    Button mEditBooksButton;

    ReadBookItemClickAdapter mItemAdapter;

    private List<BookInfo> mBookDatas;

    /**
     * 页面展示数据类型
     * 1:课本点读，2:单词宝典
     */
    private int viewType = 1;

    @Override
    public int getLayoutID() {
        return R.layout.read_activity_book;
    }

    @Override
    public void init() {
        initData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewType = bundle.getInt("view_type",1);
        }

        mToolbar.setTitle(viewType == 1 ? getString(R.string.read_book_text) : getString(R.string.word_book_text));
        mToolbar.showNavigationIcon();

        mBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mItemAdapter = new ReadBookItemClickAdapter(this, mBookDatas);
        mBookRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("position --->" + position);
                if (position == 0) {
                    Intent intent = new Intent(BookActivity.this, AddBookActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BookActivity.this, BookUnitActivity.class);
                    startActivity(intent);
                }
            }
        });

        mItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mBookDatas.remove(position);
                mItemAdapter.setEditState(false);
                mItemAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    /**
     * 测试数据
     */
    public void initData() {
        mBookDatas = new ArrayList<BookInfo>();
        for (int i = 0; i < 9; i++) {
            mBookDatas.add(new BookInfo(BookInfo.CLICK_ITEM_VIEW));
        }
    }

    @OnClick(R.id.btn_edit_books)
    public void editBooks() {
        boolean editState = mItemAdapter.getEditState();
        mItemAdapter.setEditState(!editState);
        mItemAdapter.notifyDataSetChanged();
    }
}
