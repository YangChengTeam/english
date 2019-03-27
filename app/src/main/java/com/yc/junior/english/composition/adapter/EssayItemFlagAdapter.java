package com.yc.junior.english.composition.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.composition.model.bean.VersionDetailInfo;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/23 09:47.
 */
public class EssayItemFlagAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {
    public EssayItemFlagAdapter(@Nullable List<VersionDetailInfo> data) {
        super(R.layout.essay_item_flag_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        TextView tvFlag = helper.getView(R.id.tv_flag);
        tvFlag.setText(item.getName());
        int position = helper.getAdapterPosition();
        switch (position % 4) {
            case 0:
                tvFlag.setBackgroundResource(R.drawable.essay_flag_green_bg);
                tvFlag.setTextColor(ContextCompat.getColor(mContext, R.color.green_37dd97));
                break;
            case 1:
                tvFlag.setBackgroundResource(R.drawable.essay_flag_orange_bg);
                tvFlag.setTextColor(ContextCompat.getColor(mContext, R.color.orange_ff785a));
                break;
            case 2:
                tvFlag.setBackgroundResource(R.drawable.essay_flag_purple_bg);
                tvFlag.setTextColor(ContextCompat.getColor(mContext, R.color.purple_aa78ff));
                break;
            case 3:
                tvFlag.setBackgroundResource(R.drawable.essay_flag_blue_bg);
                tvFlag.setTextColor(ContextCompat.getColor(mContext, R.color.blue_68aeff));
                break;
        }

    }
}
