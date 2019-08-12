package com.yc.junior.english.read.view.activitys;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.junior.english.base.utils.BrandUtils;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.AlertDialog;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.news.utils.ViewUtil;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.read.contract.BookContract;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.Constant;
import com.yc.junior.english.read.presenter.BookPresenter;
import com.yc.junior.english.read.view.adapter.ReadBookItemClickAdapter;
import com.yc.junior.english.vip.views.fragments.BasePayItemView;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;




/**
 * Created by admin on 2017/7/25.
 */

public class BookActivity extends FullScreenActivity<BookPresenter> implements BookContract.View, OnAdvStateListener {

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
    @BindView(R.id.iv_bottombanner_close)
    ImageView ivBottombannerClose;
    @BindView(R.id.rl_bottom_banner)
    RelativeLayout rlBottomBanner;
    @BindView(R.id.iv_topbanner_close)
    ImageView ivTopbannerClose;
    @BindView(R.id.rl_top_banner)
    RelativeLayout rlTopBanner;

    private boolean isRead = false;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_book;
    }

    @Override
    public void init() {

        mPresenter = new BookPresenter(this, this);

        if (BrandUtils.isRelatedBrand() || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())) {
            rlTopBanner.setVisibility(View.GONE);
            rlBottomBanner.setVisibility(View.GONE);


        } else {
            AdvDispatchManager.getManager().init(this, AdvType.BANNER, topBanner, null, com.yc.junior.english.main.model.domain.Constant.TENCENT_ADV_ID, com.yc.junior.english.main.model.domain.Constant.READ_TOP_BANNER, this);
            AdvDispatchManager.getManager().init(this, AdvType.BANNER, bottomBanner, null, com.yc.junior.english.main.model.domain.Constant.TENCENT_ADV_ID, com.yc.junior.english.main.model.domain.Constant.READ_BOTTOM_BANNER, this);
        }

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
        mItemAdapter.setOnItemClickListener((adapter, view, position) -> {
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
        });

        mItemAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            final AlertDialog alertDialog = new AlertDialog(BookActivity.this);
            alertDialog.setDesc("确认删除该教材？");
            alertDialog.setOnClickListener(v -> {
                alertDialog.dismiss();
                BookInfo bookInfo = mItemAdapter.getData().get(position);
                mPresenter.deleteBook(bookInfo);
            });
            alertDialog.show();
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
        RxView.clicks(ivBottombannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                rlBottomBanner.setVisibility(View.GONE);
            }
        });
        RxView.clicks(ivTopbannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                rlTopBanner.setVisibility(View.GONE);
            }
        });

    }


    //页面跳转
    public void toUnitActivity(int position, Class cls) {
        Intent intent = new Intent(BookActivity.this, cls);
        intent.putExtra("book_id", mItemAdapter.getData().get(position).getBookId());
        startActivity(intent);
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
            bookInfos = new ArrayList<>();
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
    public void onShow() {
        if (!(BrandUtils.isRelatedBrand()
                || UserInfoHelper.isVip(UserInfoHelper.getUserInfo()))) {
            ivBottombannerClose.setVisibility(View.VISIBLE);
            ivTopbannerClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(com.yc.junior.english.main.model.domain.Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void paySuccess(String info) {
        rlTopBanner.setVisibility(View.GONE);
        rlBottomBanner.setVisibility(View.GONE);
    }
}
