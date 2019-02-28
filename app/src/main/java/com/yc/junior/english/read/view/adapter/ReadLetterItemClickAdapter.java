package com.yc.junior.english.read.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.utils.DrawableUtils;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.LetterInfo;

import java.util.List;


public class ReadLetterItemClickAdapter extends BaseMultiItemQuickAdapter<LetterInfo, BaseViewHolder> {

    private Context mContext;

    private boolean isEdit = false;

    public ReadLetterItemClickAdapter(Context mContext, List<LetterInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.read_letter_item);
    }

    @SuppressLint("NewApi")
    @Override
    protected void convert(final BaseViewHolder helper, final LetterInfo item) {
        helper.setText(R.id.tv_letter, item.getLetterName());
        switch (helper.getAdapterPosition()) {
            case 0:
                ((LinearLayout) helper.getView(R.id.layout_letter)).setBackground(DrawableUtils.getBgColor(mContext,3,R.color.read_letter_color_col1));
                break;
            case 1:
                ((LinearLayout) helper.getView(R.id.layout_letter)).setBackground(DrawableUtils.getBgColor(mContext,3,R.color.read_letter_color_col2));
                break;
            case 2:
                ((LinearLayout) helper.getView(R.id.layout_letter)).setBackground(DrawableUtils.getBgColor(mContext,3,R.color.read_letter_color_col3));
                break;
            case 3:
                ((LinearLayout) helper.getView(R.id.layout_letter)).setBackground(DrawableUtils.getBgColor(mContext,3,R.color.read_letter_color_col4));
                break;
            case 4:
                ((LinearLayout) helper.getView(R.id.layout_letter)).setBackground(DrawableUtils.getBgColor(mContext,3,R.color.read_letter_color_col5));
                break;
            default:
                break;
        }
    }

}