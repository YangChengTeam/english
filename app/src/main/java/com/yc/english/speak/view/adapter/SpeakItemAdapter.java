package com.yc.english.speak.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.speak.model.bean.SpeakEnglishBean;

import java.util.List;

public class SpeakItemAdapter extends BaseQuickAdapter<SpeakEnglishBean, BaseViewHolder> {

    private Context mContext;

    public SpeakItemAdapter(Context context, List<SpeakEnglishBean> datas) {
        super(R.layout.speak_listen_english_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SpeakEnglishBean item) {
        helper.setText(R.id.tv_english_sen, item.getEnSentence())
                .setText(R.id.tv_chinese_sen, item.getCnSentence());

        if (item.isShowSpeak()) {
            helper.setVisible(R.id.speak_layout, true);
            helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setVisible(R.id.speak_layout, false);
            helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.listen_item_bg_color));
        }

        if (item.isShowResult()) {
            helper.setVisible(R.id.layout_result, true);
        } else {
            helper.setVisible(R.id.layout_result, false);
        }

        if (item.isSpeakResult()) {
            helper.setBackgroundRes(R.id.iv_result, R.mipmap.listen_result_yes);
        } else {
            helper.setBackgroundRes(R.id.iv_result, R.mipmap.listen_result_no);
        }

        helper.addOnClickListener(R.id.iv_play_read)
                .addOnClickListener(R.id.play_layout)
                .addOnClickListener(R.id.iv_speak_tape)
                .addOnClickListener(R.id.speak_tape_layout)
                .addOnClickListener(R.id.iv_play_self_speak)
                .addOnClickListener(R.id.play_speak_tape_layout);
    }
}