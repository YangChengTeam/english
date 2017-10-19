package com.yc.english.speak.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.speak.model.bean.ListenEnglishBean;

import java.util.List;

public class ListenEnglishItemAdapter extends BaseQuickAdapter<ListenEnglishBean, BaseViewHolder> {

    private Context mContext;

    public ListenEnglishItemAdapter(Context context, List<ListenEnglishBean> datas) {
        super(R.layout.speak_listen_english_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ListenEnglishBean item) {
        helper.setText(R.id.tv_english_sen, item.getEnSentence())
                .setText(R.id.tv_chinese_sen, item.getCnSentence());
        if (item.isShowSpeak()) {
            helper.setVisible(R.id.speak_layout, true);
            helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.white));
        } else {
            helper.setVisible(R.id.speak_layout, false);
            helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.listen_item_bg_color));
        }

        helper.addOnClickListener(R.id.iv_speak_tape).addOnClickListener(R.id.speak_tape_layout).addOnClickListener(R.id.iv_play_self_speak).addOnClickListener(R.id.play_speak_tape_layout);
    }
}