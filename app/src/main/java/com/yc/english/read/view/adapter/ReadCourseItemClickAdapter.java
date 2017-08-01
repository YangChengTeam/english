package com.yc.english.read.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.EnglishCourseInfo;

import java.util.List;


public class ReadCourseItemClickAdapter extends BaseMultiItemQuickAdapter<EnglishCourseInfo, BaseViewHolder> {

    private Context mContext;

    private int languageType = 1;

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
    }

    public ReadCourseItemClickAdapter(Context mContext, List<EnglishCourseInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(EnglishCourseInfo.CLICK_ITEM_VIEW, R.layout.read_course_play_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EnglishCourseInfo item) {

        helper.setText(R.id.tv_chinese_title, item.getSubtitlecn())
                .setText(R.id.tv_english_title, item.getSubtitle())
                .addOnClickListener(R.id.layout_play);

        switch (languageType) {
            case 1:
                helper.setVisible(R.id.tv_chinese_title,true).setVisible(R.id.tv_english_title,true);
                break;
            case 2:
                helper.setVisible(R.id.tv_chinese_title,false).setVisible(R.id.tv_english_title,true);
                break;
            case 3:
                helper.setVisible(R.id.tv_chinese_title,true).setVisible(R.id.tv_english_title,false);
                break;
            default:
                break;
        }

    }

}