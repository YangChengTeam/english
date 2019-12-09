package com.yc.junior.english.composition.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.composition.model.bean.CompositionInfo;
import com.yc.junior.english.composition.widget.CompositionRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

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
