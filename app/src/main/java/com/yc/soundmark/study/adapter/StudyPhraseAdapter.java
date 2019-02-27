package com.yc.soundmark.study.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.soundmark.study.model.domain.PhraseInfo;
import com.yc.soundmark.study.utils.LPUtils;

import java.util.List;

/**
 * Created by wanglin  on 2018/10/31 14:28.
 */
public class StudyPhraseAdapter extends BaseQuickAdapter<PhraseInfo, BaseViewHolder> {
    private SparseArray<ImageView> sparseArray;
    private SparseArray<RelativeLayout> layoutSparseArray;

    public StudyPhraseAdapter(List<PhraseInfo> data) {
        super(R.layout.study_phrase_item, data);
        sparseArray = new SparseArray<>();
        layoutSparseArray = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, PhraseInfo item) {
        helper.setText(R.id.tv_phrase_chinese, item.getEn())
                .setText(R.id.tv_phrase_english, Html.fromHtml(LPUtils.getInstance().addPhraseLetterColor(item.getPhrase())))
                .addOnClickListener(R.id.ll_play)
                .addOnClickListener(R.id.ll_record)
                .addOnClickListener(R.id.ll_record_playback);

        int adapterPosition = helper.getAdapterPosition();
        sparseArray.put(adapterPosition, (ImageView) helper.getView(R.id.iv_loading));
        layoutSparseArray.put(adapterPosition, (RelativeLayout) helper.getView(R.id.rl_action_container));
        if (adapterPosition == 0) {
            helper.setVisible(R.id.rl_action_container, true);
        }

    }

    public void startAnimation(int position) {
        resetDrawable();
        if (sparseArray != null) {
            ImageView imageView = sparseArray.get(position);
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
            imageView.setVisibility(View.VISIBLE);
            animationDrawable.start();
        }
    }


    public void resetDrawable() {
        if (sparseArray != null) {
            for (int i = 0; i < sparseArray.size(); i++) {
                ImageView imageView = sparseArray.get(i);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.stop();
                imageView.setVisibility(View.GONE);
            }
        }
    }

    private void hideActionContainer() {
        if (layoutSparseArray != null) {
            for (int i = 0; i < layoutSparseArray.size(); i++) {
                layoutSparseArray.get(i).setVisibility(View.GONE);
            }
        }
    }

    public void showActionContainer(int position) {
        hideActionContainer();
        if (layoutSparseArray != null) {
            layoutSparseArray.get(position).setVisibility(View.VISIBLE);
        }
    }
}
