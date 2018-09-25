package com.yc.english.read.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.read.model.domain.BookInfo;

import java.util.List;


public class ReadBookItemClickAdapter extends BaseMultiItemQuickAdapter<BookInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    private List<BookInfo> datas;

    public ReadBookItemClickAdapter(Context mContext, List<BookInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_book_item);
    }

    public List<BookInfo> getDatas() {
        return datas;
    }

    public void setDatas(List<BookInfo> datas) {
        this.datas = datas;
    }

    public boolean getEditState() {
        return this.isEdit;
    }

    public void setEditState(boolean editState) {
        this.isEdit = editState;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BookInfo item) {

        helper.addOnClickListener(R.id.iv_delete_book);

        ImageView mBookCover = helper.getView(R.id.iv_book_cover);
        ImageView mDeleteBook = helper.getView(R.id.iv_delete_book);

        helper.setText(R.id.tv_book_version_name, item.getVersionName()).setText(R.id.tv_book_grade_name, item.getGradeName());

        if (helper.getAdapterPosition() > 0) {
            GlideHelper.imageView(mContext, mBookCover, item.getCoverImg(), R.mipmap.default_book);
        } else {
            mBookCover.setImageResource(R.mipmap.read_book_add);
            showJoinGuide(mBookCover);
        }

        if (this.isEdit) {
            if (helper.getAdapterPosition() > 0) {
                mDeleteBook.setVisibility(View.VISIBLE);
            } else {
                mDeleteBook.setVisibility(View.INVISIBLE);
            }
        } else {
            mDeleteBook.setVisibility(View.INVISIBLE);
        }
    }


    private void showJoinGuide(View view) {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        final GuidePopupWindow guideAddBookPopupWindow = builder.setDelay(0).setTargetView(view).setCorner(5).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {
//                goToActivity(GroupJoinActivity.class);
            }
        })
                .build(((Activity) mContext));
        guideAddBookPopupWindow.addCustomView(R.layout.add_book_guide, R.id.m_btn_OK, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideAddBookPopupWindow.dismiss();
            }
        });
//        guideAddBookPopupWindow.setDebug(true);
        guideAddBookPopupWindow.show(((Activity) mContext).getWindow().getDecorView(), "add book");
    }
}