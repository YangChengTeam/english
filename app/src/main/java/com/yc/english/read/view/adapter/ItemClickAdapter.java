package com.yc.english.read.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.read.domain.EnglishCourse;

import java.util.List;


public class ItemClickAdapter extends BaseMultiItemQuickAdapter<EnglishCourse, BaseViewHolder>  {

    private Context mContext;

    public ItemClickAdapter(Context mContext, List<EnglishCourse> data) {
        super(data);
        this.mContext = mContext;
        addItemType(EnglishCourse.CLICK_ITEM_VIEW, R.layout.read_course_play_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EnglishCourse item) {

        helper.setText(R.id.tv_chinese_title, item.getSubtitlecn())
                .setText(R.id.tv_english_title, item.getSubtitle())
                .addOnClickListener(R.id.layout_play);
        if(helper.getAdapterPosition() == 3){
            ((ImageView)helper.getView(R.id.iv_audio_play)).setVisibility(View.VISIBLE);
        }
    }

}