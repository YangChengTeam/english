package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.CommonInfo;

import java.util.List;


public class ReadAddBookItemClickAdapter extends BaseMultiItemQuickAdapter<CommonInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    public ReadAddBookItemClickAdapter(Context mContext, List<CommonInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_add_book_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommonInfo item) {
        helper.setText(R.id.btn_read_grade_name, item.getCommonName()).addOnClickListener(R.id.iv_delete_book);
    }
}