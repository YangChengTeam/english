package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.utils.LogUtil;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.utils.PropertyUtil;
import com.yc.english.base.utils.TencentAdvManager;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.read.model.domain.Constant;
import com.yc.english.news.utils.ViewUtil;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.contract.BookContract;
import com.yc.english.read.model.domain.BookInfo;

import com.yc.english.read.presenter.BookPresenter;
import com.yc.english.read.view.adapter.ReadBookItemClickAdapter;
import com.yc.english.vip.views.fragments.BasePayItemView;

import java.util.ArrayList;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/25.
 */

public class BookActivity extends FullScreenActivity<BookPresenter> implements BookContract.View {

    @BindView(R.id.rv_book_list)
    RecyclerView mBookRecyclerView;

    @BindView(R.id.btn_edit_books)
    Button mEditBooksButton;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.ll_content)
    LinearLayout mContentLinearLayout;

    @BindView(R.id.baseItemView_textbook_read)
    BasePayItemView baseItemViewTextbookRead;
    @BindView(R.id.baseItemView_word_valuable)
    BasePayItemView baseItemViewWordValuable;
    @BindView(R.id.baseItemView_brainpower_appraisal)
    BasePayItemView baseItemViewBrainpowerAppraisal;
    @BindView(R.id.baseItemView_score_tutorship)
    BasePayItemView baseItemViewScoreTutorship;


    ReadBookItemClickAdapter mItemAdapter;
    @BindView(R.id.topBanner)
    FrameLayout topBanner;
    @BindView(R.id.bottomBanner)
    FrameLayout bottomBanner;

    private boolean isRead = false;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_book;
    }

    @Override
    public void init() {

        mPresenter = new BookPresenter(this, this);
        Properties pt = PropertyUtil.getProperties(this);

        if (TextUtils.equals("true", pt.getProperty("isXiaomi"))) {
            topBanner.setVisibility(View.GONE);
            bottomBanner.setVisibility(View.GONE);
        }
        TencentAdvManager.showBannerAdv(this, topBanner, com.yc.english.main.model.domain.Constant.READ_TOP_BANNER);
        TencentAdvManager.showBannerAdv(this, bottomBanner, com.yc.english.main.model.domain.Constant.READ_BOTTOM_BANNER);

        String titleName;
        if (ReadApp.READ_COMMON_TYPE == 1) {
            titleName = getString(R.string.read_book_text);
            baseItemViewTextbookRead.setContentAndIcon(getString(R.string.weike_every_day_text), R.mipmap.everyday_weike);
            isRead = true;
        } else if (ReadApp.READ_COMMON_TYPE == 2) {
            titleName = getString(R.string.word_book_text);
            baseItemViewWordValuable.setContentAndIcon(getString(R.string.weike_every_day_text), R.mipmap.everyday_weike);
            isRead = false;
        } else {
            titleName = getString(R.string.word_game_text);
        }

        mToolbar.setTitle(titleName);
        mToolbar.showNavigationIcon();

        mBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mItemAdapter = new ReadBookItemClickAdapter(this, null);
        mBookRecyclerView.setAdapter(mItemAdapter);


        initListener();


    }

    private void initListener() {
        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mItemAdapter.getEditState()) {
                    if (position == 0) {
                        Intent intent = new Intent(BookActivity.this, AddBookActivity.class);
                        startActivity(intent);
                    } else {
                        if (ReadApp.READ_COMMON_TYPE == 1) {
                            toUnitActivity(position, BookUnitActivity.class);
                        } else {
                            toUnitActivity(position, WordUnitActivity.class);
                        }
                    }
                }
            }
        });

        mItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final AlertDialog alertDialog = new AlertDialog(BookActivity.this);
                alertDialog.setDesc("确认删除该教材？");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        BookInfo bookInfo = (BookInfo) mItemAdapter.getData().get(position);
                        mPresenter.deleteBook(bookInfo);
                    }
                });
                alertDialog.show();
                return false;
            }
        });
        if (isRead) {
            ViewUtil.switchActivity(this, baseItemViewTextbookRead, 4);
            ViewUtil.switchActivity(this, baseItemViewWordValuable, 1);
        } else {
            ViewUtil.switchActivity(this, baseItemViewTextbookRead, 0);
            ViewUtil.switchActivity(this, baseItemViewWordValuable, 4);
        }
        ViewUtil.switchActivity(this, baseItemViewBrainpowerAppraisal, 2);
        ViewUtil.switchActivity(this, baseItemViewScoreTutorship, 3);


    }


    //页面跳转
    public void toUnitActivity(int position, Class cls) {
        if (mItemAdapter.getData() != null) {
            Intent intent = new Intent(BookActivity.this, cls);
            intent.putExtra("book_id", ((BookInfo) mItemAdapter.getData().get(position)).getBookId());
            startActivity(intent);
        } else {
            TipsHelper.tips(BookActivity.this, "数据异常，请稍后重试");
        }
    }

    @OnClick(R.id.btn_edit_books)
    public void editBooks() {
        boolean editState = mItemAdapter.getEditState();

        mItemAdapter.setEditState(!editState);
        mItemAdapter.notifyDataSetChanged();
        if (mItemAdapter.getEditState()) {
            mEditBooksButton.setText(getString(R.string.read_book_edit_done_text));
            mEditBooksButton.setBackgroundResource(R.drawable.read_word_share_btn);
            mEditBooksButton.setTextColor(ContextCompat.getColor(BookActivity.this, R.color.white));
        } else {
            mEditBooksButton.setText(getString(R.string.read_book_edit_text));
            mEditBooksButton.setBackgroundResource(R.drawable.read_border_line_btn);
            mEditBooksButton.setTextColor(ContextCompat.getColor(BookActivity.this, R.color.read_book_edit_color));
        }
    }

    @Override
    public void showBookListData(ArrayList<BookInfo> bookInfos, boolean isEdit) {
        //TODO,数据处理待完成
        LogUtils.e("Add Book --->");
        if (bookInfos == null) {
            bookInfos = new ArrayList<BookInfo>();
        }
        mItemAdapter.setEditState(isEdit);
        bookInfos.add(0, new BookInfo(BookInfo.CLICK_ITEM_VIEW));
        if (bookInfos.size() == 1) {
            mItemAdapter.setEditState(true);
            editBooks();
        }
        mItemAdapter.setNewData(bookInfos);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.ADD_BOOK_INFO)
            }
    )

    @Override
    public void addBook(BookInfo bookInfo) {
        mPresenter.addBook(bookInfo);
    }

    @Override
    public void deleteBookRefresh() {
        //TipsHelper.tips(BookActivity.this, "删除成功");
    }

    @Override
    public void hide() {
        mLoadingStateView.hide();
    }

    @Override
    public void showLoading() {
        mLoadingStateView.showLoading(mContentLinearLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
