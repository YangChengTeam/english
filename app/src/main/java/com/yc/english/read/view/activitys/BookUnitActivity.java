package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.read.contract.BookUnitContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.UnitInfo;
import com.yc.english.read.model.domain.UnitInfoList;
import com.yc.english.read.presenter.BookUnitPresenter;
import com.yc.english.read.view.adapter.ReadBookUnitItemClickAdapter;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;
import com.yc.english.setting.view.activitys.BuyVipActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class BookUnitActivity extends FullScreenActivity<BookUnitPresenter> implements BookUnitContract.View {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    LinearLayout mLayoutContext;

    @BindView(R.id.iv_book_grade)
    ImageView mBookGradeImageView;

    @BindView(R.id.tv_book_grade_name)
    TextView mBookGradeNameTextView;

    @BindView(R.id.tv_book_press)
    TextView mBookPressTextView;

    @BindView(R.id.tv_book_unit_total)
    TextView mBookUnitTotalTextView;

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

                boolean isRead = true;

                UnitInfo unitInfo = mItemAdapter.getData().get(position);
                //1是免费，2是收费
                if (unitInfo.getFree() == 1) {
                    isRead = true;
                } else {
                    if(UserInfoHelper.getUserInfo() != null) {
                        if (UserInfoHelper.getUserInfo().getIsVip() == 1) {
                            isRead = true;
                        } else {
                            isRead = false;
                        }
                    }else{
                        UserInfoHelper.isGotoLogin(BookUnitActivity.this);
                        return;
                    }
                }

                if (isRead) {
                    if (mItemAdapter.getData() != null && mItemAdapter.getData().get(position) != null) {
                        Intent intent = new Intent(BookUnitActivity.this, CoursePlayActivity.class);
                        intent.putExtra("unit_id", ((UnitInfo) mItemAdapter.getData().get(position)).getId());
                        intent.putExtra("unit_title", ((UnitInfo) mItemAdapter.getData().get(position)).getName());
                        intent.putExtra("last_unit_ids", getLastUnitIds(position));
                        intent.putExtra("last_unit_titles", getLastUnitTitles(position));
                        startActivity(intent);
                    } else {
                        TipsHelper.tips(BookUnitActivity.this, "教材数据异常，请稍后重试");
                    }
                } else {
                    final AlertDialog alertDialog = new AlertDialog(BookUnitActivity.this);
                    alertDialog.setDesc("请购买会员使用点读功能？");
                    alertDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(BookUnitActivity.this, BuyVipActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getBookInfoById(bookId);
    }

    public String getLastUnitIds(int position) {
        int pos = position + 1;
        if (mItemAdapter.getData() != null && pos < mItemAdapter.getData().size()) {
            StringBuffer lastUnitIds = new StringBuffer("");
            for (int i = pos; i < mItemAdapter.getData().size(); i++) {
                lastUnitIds.append(((UnitInfo) mItemAdapter.getData().get(i)).getId()).append(",");
            }
            if (!lastUnitIds.equals("")) {
                return lastUnitIds.toString();
            }
        }
        return null;
    }

    public String getLastUnitTitles(int position) {
        int pos = position + 1;
        if (mItemAdapter.getData() != null && pos < mItemAdapter.getData().size()) {
            StringBuffer lastUnitTitles = new StringBuffer("");
            for (int i = pos; i < mItemAdapter.getData().size(); i++) {
                lastUnitTitles.append(((UnitInfo) mItemAdapter.getData().get(i)).getName()).append("#");
            }
            if (!lastUnitTitles.equals("")) {
                return lastUnitTitles.toString();
            }
        }
        return null;
    }

    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getBookInfoById(bookId);
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
            GlideHelper.imageView(BookUnitActivity.this, mBookGradeImageView, bookInfo.getCoverImg(), R.mipmap.default_detail_book);
            mBookGradeNameTextView.setText(bookInfo.getName());
            mBookPressTextView.setText(bookInfo.getPress());
            mBookUnitTotalTextView.setText(bookInfo.getSentenceCount() + getString(R.string.read_sentence_text));
        }
    }
}
