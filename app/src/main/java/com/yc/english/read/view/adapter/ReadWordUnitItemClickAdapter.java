package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.UnitListInfo;

import java.util.List;


public class ReadWordUnitItemClickAdapter extends BaseMultiItemQuickAdapter<UnitListInfo, BaseViewHolder> {

    private Context mContext;

    public ReadWordUnitItemClickAdapter(Context mContext, List<UnitListInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_word_unit_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UnitListInfo item) {
        helper.setText(R.id.tv_book_unit_name, item.getUnitTitle())
                .setText(R.id.tv_book_unit_total, item.getUnitTotal())
                .setText(R.id.tv_total_persion_recite, item.getReciteTotalPersion());
    }
}