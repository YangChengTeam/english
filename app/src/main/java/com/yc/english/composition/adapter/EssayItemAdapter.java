package com.yc.english.composition.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.composition.model.bean.CompositionInfo;
import com.yc.english.composition.widget.CompositionRecyclerView;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wanglin  on 2019/3/23 09:24.
 */
public class EssayItemAdapter extends BaseQuickAdapter<CompositionInfo, BaseViewHolder> {
    private boolean mIsMore;

    public EssayItemAdapter(@Nullable List<CompositionInfo> data, boolean isMore) {
        super(R.layout.essay_item_view, data);
        this.mIsMore = isMore;
    }

    @Override
    protected void convert(BaseViewHolder helper, CompositionInfo item) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        helper.setText(R.id.tv_item_title, item.getTitle())
                .setText(R.id.tv_item_date, simpleDateFormat.format(new Date(item.getAddtime() * 1000)))
                .setText(R.id.tv_read_num, String.format(mContext.getString(R.string.read_num), item.getReadnum()));
        ImageView iv = helper.getView(R.id.iv_item_cover);

        GlideHelper.imageView(mContext, iv, item.getImg(), 0);

        CompositionRecyclerView recyclerView = helper.getView(R.id.item_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new EssayItemFlagAdapter(item.getFlags()));
        if (!mIsMore)
            if (helper.getAdapterPosition() == mData.size() - 1) {
                helper.setGone(R.id.item_divider, false);
            }

    }
}
