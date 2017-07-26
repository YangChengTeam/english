package com.yc.english.read.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.model.domain.EnglishCourseInfo;

import java.util.List;


public class ReadCourseItemClickAdapter extends BaseMultiItemQuickAdapter<EnglishCourseInfo, BaseViewHolder>  {

    private Context mContext;

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
        if(helper.getAdapterPosition() == 3){
            ((ImageView)helper.getView(R.id.iv_audio_play)).setVisibility(View.VISIBLE);
            ((TextView)helper.getView(R.id.tv_chinese_title)).setTextColor(Color.parseColor("#333333"));
            ((TextView)helper.getView(R.id.tv_english_title)).setTextColor(Color.parseColor("#333333"));
        }
    }

}