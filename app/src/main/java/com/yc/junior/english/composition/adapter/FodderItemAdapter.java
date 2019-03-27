package com.yc.junior.english.composition.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.composition.model.bean.CompositionInfo;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/25 09:50.
 */
public class FodderItemAdapter extends BaseQuickAdapter<CompositionInfo, BaseViewHolder> {
    public FodderItemAdapter(@Nullable List<CompositionInfo> data) {
        super(R.layout.fodder_item_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CompositionInfo item) {
        helper.setText(R.id.tv_sub_title, item.getSub_title())
                .setText(R.id.tv_desc, item.getFodderDesp());
        if (!TextUtils.isEmpty(item.getFodderIcon())) {
            ImageView imageView = helper.getView(R.id.iv_cover);
            GlideHelper.imageView(mContext, imageView, item.getFodderIcon(), 0);
        }
        if (helper.getAdapterPosition() == mData.size() - 1) {
            helper.setGone(R.id.view_divider, false);
        }
    }
}
