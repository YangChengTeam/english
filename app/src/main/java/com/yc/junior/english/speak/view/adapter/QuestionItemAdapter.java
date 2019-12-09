package com.yc.junior.english.speak.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.speak.model.bean.QuestionInfoBean;

import java.util.List;

import androidx.core.content.ContextCompat;
import yc.com.blankj.utilcode.util.StringUtils;

public class QuestionItemAdapter extends BaseMultiItemQuickAdapter<QuestionInfoBean, BaseViewHolder> {

    private Context mContext;

    private boolean isFirst;

    public QuestionItemAdapter(Context context, List datas, boolean isFirst) {
        super(datas);
        this.mContext = context;
        this.isFirst = isFirst;
        addItemType(QuestionInfoBean.MAIN_QUESTION, R.layout.speak_listen_english_item);
        addItemType(QuestionInfoBean.OPTION_QUESTION, R.layout.speak_main_item);
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionInfoBean item) {
        switch (helper.getItemViewType()) {
            case QuestionInfoBean.OPTION_QUESTION:
                helper.setText(R.id.tv_title, item.getTitle());
                break;
            case QuestionInfoBean.MAIN_QUESTION:

                helper.setText(R.id.tv_english_sen, item.getEnSentence()).setText(R.id.tv_chinese_sen, item.getCnSentence());

                if (helper.getAdapterPosition() == 1 && isFirst) {
                    item.setShowSpeak(true);
                }

                if (item.isShowSpeak()) {
                    helper.setGone(R.id.speak_layout, true);
                    helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.white));
                } else {
                    helper.setGone(R.id.speak_layout, false);
                    helper.setBackgroundColor(R.id.listen_layout, ContextCompat.getColor(mContext, R.color.listen_item_bg_color));
                }

                LinearLayout resultLayout = helper.getConvertView().findViewById(R.id.layout_result);
                if (item.isShowResult()) {
                    resultLayout.setVisibility(View.VISIBLE);
                } else {
                    resultLayout.setVisibility(View.INVISIBLE);
                }

                int percent = 0;
                if (!StringUtils.isEmpty(item.getPercent())) {
                    percent = (int) Double.parseDouble(item.getPercent());
                }

                if (item.isSpeakResult()) {
                    helper.setText(R.id.tv_result_hint, percent + "分,Good");
                    helper.setBackgroundRes(R.id.iv_result, R.mipmap.listen_result_yes);
                } else {
                    helper.setBackgroundRes(R.id.iv_result, R.mipmap.listen_result_no);
                    helper.setText(R.id.tv_result_hint, percent + "分,继续加油");
                }

                helper.addOnClickListener(R.id.iv_play_read)
                        .addOnClickListener(R.id.play_layout)
                        .addOnClickListener(R.id.iv_speak_tape)
                        .addOnClickListener(R.id.speak_tape_layout)
                        .addOnClickListener(R.id.iv_play_self_speak)
                        .addOnClickListener(R.id.play_speak_tape_layout);
                break;
            default:
                break;
        }
    }

}