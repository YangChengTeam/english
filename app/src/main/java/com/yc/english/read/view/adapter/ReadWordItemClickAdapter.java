package com.yc.english.read.view.adapter;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.R;
import com.yc.english.read.model.domain.WordDetailInfo;
import com.yc.english.read.model.domain.WordInfo;

import java.util.List;


public class ReadWordItemClickAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;

    public static final int TYPE_LEVEL_1 = 1;

    private Context mContext;

    public interface ItemDetailClick {
        public void detailClick(int position);
    }

    ItemDetailClick itemDetailClick;

    public void setItemDetailClick(ItemDetailClick itemDetailClick) {
        this.itemDetailClick = itemDetailClick;
    }

    public ReadWordItemClickAdapter(Context mContext, List<MultiItemEntity> data) {
        super(data);
        this.mContext = mContext;
        addItemType(TYPE_LEVEL_0, R.layout.read_word_play_item);
        addItemType(TYPE_LEVEL_1, R.layout.read_word_play_item_detail);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:

                final WordInfo wordInfo = (WordInfo) item;
                helper.setText(R.id.tv_word_number, (helper.getAdapterPosition() + 1) + "")
                        .setText(R.id.tv_en_word, wordInfo.getWord())
                        .setText(R.id.tv_cn_word, wordInfo.getMeans())
                        .addOnClickListener(R.id.layout_read_word_audio);
                int pos = helper.getAdapterPosition();
                if (wordInfo.isExpanded()) {
                    helper.itemView.setBackgroundResource(R.mipmap.read_word_item_selected);
                } else {
                    helper.itemView.setBackgroundResource(R.mipmap.read_word_item_normal);
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        LogUtils.e("pos: " + pos);

                        if (wordInfo.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });

                break;
            case TYPE_LEVEL_1:
                final WordDetailInfo wordDetailInfo = (WordDetailInfo) item;
                helper.setText(R.id.tv_en_word_detail, wordDetailInfo.getWordExample()).setText(R.id.tv_cn_word_detail, wordDetailInfo.getWordCnExample());

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemDetailClick.detailClick(helper.getAdapterPosition());
                    }
                });

                break;
        }
    }
}