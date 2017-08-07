package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.contract.BookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.Constant;
import com.yc.english.read.presenter.BookPresenter;
import com.yc.english.read.view.adapter.ReadBookItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/25.
 */

public class BookActivity extends FullScreenActivity<BookPresenter> implements BookContract.View {

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

    /**
     * 当前页码
     */
    private int currentPage;
    /**
     * 每一页数据记录数
     */
    private int pageCount;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_book;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewType = bundle.getInt("view_type", 1);
        }

        mPresenter = new BookPresenter(this, this);

        String titleName = getString(R.string.read_book_text);
        if (viewType == 1) {
            titleName = getString(R.string.read_book_text);
        } else if (viewType == 2) {
            titleName = getString(R.string.word_book_text);
        } else {
            titleName = getString(R.string.word_game_text);
        }

        mToolbar.setTitle(titleName);
        mToolbar.showNavigationIcon();

        mBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mItemAdapter = new ReadBookItemClickAdapter(this, null);
        mBookRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("position --->" + position);

                if (!mItemAdapter.getEditState()) {
                    if (position == 0) {
                        Intent intent = new Intent(BookActivity.this, AddBookActivity.class);
                        intent.putExtra("view_type", viewType);
                        startActivity(intent);
                    } else {
                        if (viewType == 1) {
                            Intent intent = new Intent(BookActivity.this, BookUnitActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(BookActivity.this, WordUnitActivity.class);
                            intent.putExtra("view_type", viewType);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

        mItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mBookDatas != null && mBookDatas.size() > 0) {
                    mBookDatas.remove(position);
                }
                //mItemAdapter.setEditState(false);
                mItemAdapter.notifyDataSetChanged();
                return false;
            }
        });

        mPresenter.bookList(currentPage, pageCount);
    }


    @OnClick(R.id.btn_edit_books)
    public void editBooks() {
        boolean editState = mItemAdapter.getEditState();

        mItemAdapter.setEditState(!editState);
        mItemAdapter.notifyDataSetChanged();
        if (mItemAdapter.getEditState()) {
            mEditBooksButton.setText(getString(R.string.read_book_edit_done_text));
        } else {
            mEditBooksButton.setText(getString(R.string.read_book_edit_text));
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.BOOK_INFO_LIST)
            }
    )
    @Override
    public void showBookListData(ArrayList<BookInfo> list) {
        //TODO,数据处理待完成
        if(list == null || list.size() == 0){
            mBookDatas = new ArrayList<BookInfo>();
            for (int i = 0; i < 1; i++) {
                mBookDatas.add(new BookInfo(BookInfo.CLICK_ITEM_VIEW));
            }
            list = (ArrayList<BookInfo>) mBookDatas;
        }

        mItemAdapter.setNewData(list);
        mItemAdapter.notifyDataSetChanged();
    }
}
