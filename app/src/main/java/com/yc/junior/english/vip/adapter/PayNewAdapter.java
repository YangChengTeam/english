package com.yc.junior.english.vip.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.vip.model.bean.VipGoodInfo;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/13 08:32.
 */
public class PayNewAdapter extends BaseQuickAdapter<VipGoodInfo, BaseViewHolder> {
    public PayNewAdapter(@Nullable List<VipGoodInfo> data) {
        super(R.layout.pay_new_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipGoodInfo item) {
        helper.setText(R.id.tv_item_title, item.getTitle())
                .setText(R.id.tv_item_content, item.getContent());
        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = 0;
            helper.itemView.setLayoutParams(layoutParams);

        }
    }
}
