package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.WordUnitInfo;

import java.util.List;


public class ReadWordUnitItemClickAdapter extends BaseMultiItemQuickAdapter<WordUnitInfo, BaseViewHolder> {

    private Context mContext;

    public ReadWordUnitItemClickAdapter(Context mContext, List<WordUnitInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_word_unit_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WordUnitInfo item) {
        helper.setText(R.id.tv_book_unit_name, item.getName())
                .setText(R.id.tv_book_unit_total, item.getWordCount() + mContext.getString(R.string.word_sentence_count_text))
                .setText(R.id.tv_total_persion_recite, item.getNumber() + mContext.getString(R.string.word_persion_sentence_count_text));
    }
}