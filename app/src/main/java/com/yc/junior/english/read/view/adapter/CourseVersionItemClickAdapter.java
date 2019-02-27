package com.yc.junior.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.CourseVersionInfo;

import java.util.List;


public class CourseVersionItemClickAdapter extends BaseMultiItemQuickAdapter<CourseVersionInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    public CourseVersionItemClickAdapter(Context mContext, List<CourseVersionInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_add_book_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CourseVersionInfo item) {
        helper.setText(R.id.btn_read_grade_name, item.getVersionName());

        if (!item.isAdd()) {
            helper.setBackgroundRes(R.id.btn_read_grade_name, R.drawable.read_add_book_checked_btn);
            helper.getConvertView().setClickable(false);
        } else {
            helper.getConvertView().setClickable(true);
            if (item.isSelected()) {
                helper.getView(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_select_line_btn);
            } else {
                helper.getView(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_line_btn);
            }

        }
    }
}