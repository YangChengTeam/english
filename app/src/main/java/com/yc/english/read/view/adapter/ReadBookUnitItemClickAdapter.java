package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.UnitInfo;

import java.util.List;


public class ReadBookUnitItemClickAdapter extends BaseMultiItemQuickAdapter<UnitInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    public ReadBookUnitItemClickAdapter(Context mContext, List<UnitInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_book_unit_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UnitInfo item) {
        helper.setText(R.id.tv_book_unit_name, item.getUnitTitle())
                .setText(R.id.tv_book_unit_total, item.getUnitTotal())
                .addOnClickListener(R.id.iv_delete_book);
    }
}