package com.yc.english.read.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
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

        ImageView mBookCover = (ImageView) helper.getView(R.id.iv_book_cover);
        ImageView mDeleteBook = (ImageView) helper.getView(R.id.iv_delete_book);

        helper.setText(R.id.tv_book_version_name,item.getVersionName()).setText(R.id.tv_book_grade_name,item.getGradeName());

        if (helper.getAdapterPosition() > 0) {
            Glide.with(mContext).load(item.getCoverImg()).into(mBookCover);
        } else {
            mBookCover.setImageResource(R.mipmap.read_book_add);
        }

        if (this.isEdit) {
            if (helper.getAdapterPosition() > 0) {
                mDeleteBook.setVisibility(View.VISIBLE);
            }
        } else {
            mDeleteBook.setVisibility(View.INVISIBLE);
        }
    }

}