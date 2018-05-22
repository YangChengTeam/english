package com.yc.junior.english.read.view.adapter;

import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.junior.english.R;
import com.yc.junior.english.read.model.domain.WordDetailInfo;
import com.yc.junior.english.read.model.domain.WordInfo;

import java.util.List;


public class ReadWordItemClickAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_0 = 0;

    public static final int TYPE_LEVEL_1 = 1;

    private Context mContext;

    public interface ItemDetailClick {
        public void wordDetailClick(int position, String sentenceSimple);
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
                final int pos = helper.getLayoutPosition();

                helper.setText(R.id.tv_word_number, (pos + 1) + "")
                        .setText(R.id.tv_en_word, wordInfo.getName())
                        .setText(R.id.tv_cn_word, wordInfo.getMeans())
                        .addOnClickListener(R.id.layout_read_word_audio);
                if (wordInfo.isExpanded()) {
                    helper.itemView.setBackgroundResource(R.mipmap.read_word_item_selected);
                } else {
                    helper.itemView.setBackgroundResource(R.mipmap.read_word_item_normal);
                }

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //final int pos = helper.getAdapterPosition();

                        LogUtils.e("pos: " + pos);

                        if (wordInfo.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }

                        for (int i = 0; i < getData().size(); i++) {
                            if (getData().get(i).getItemType() == TYPE_LEVEL_0) {
                                WordInfo wordInfo = (WordInfo) getData().get(i);
                                if (pos != i && wordInfo.isExpanded()) {
                                    collapse(i);
                                    break;
                                }
                            }
                        }

                        //helper.setText(R.id.tv_word_number, (pos + 1) + "");
                    }
                });

                break;
            case TYPE_LEVEL_1:
                final WordDetailInfo wordInfoDetail = (WordDetailInfo) item;
                helper.setText(R.id.tv_en_word_detail, wordInfoDetail.getWordExample()).setText(R.id.tv_cn_word_detail, wordInfoDetail.getWordCnExample());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showLong("pp--->" + getParentPosition(wordInfoDetail));
                        itemDetailClick.wordDetailClick(helper.getLayoutPosition(), wordInfoDetail.getWordExample());
                    }
                });
                break;
        }
    }
}