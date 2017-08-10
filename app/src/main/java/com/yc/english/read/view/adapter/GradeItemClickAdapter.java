package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.GradeInfo;

import java.util.List;


public class GradeItemClickAdapter extends BaseMultiItemQuickAdapter<GradeInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    public GradeItemClickAdapter(Context mContext, List<GradeInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_add_book_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GradeInfo item) {
        helper.setText(R.id.btn_read_grade_name, item.getName());
        if (item.isSelected()) {
            helper.getView(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_select_line_btn);
        } else {
            helper.getView(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_line_btn);
        }
    }
}