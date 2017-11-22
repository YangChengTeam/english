package com.yc.english.weixin.views.adapters;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.WeiKeInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKeInfoItemAdapter extends BaseQuickAdapter<WeiKeInfo, BaseViewHolder> {
    private String mType;

    public WeiKeInfoItemAdapter(List<WeiKeInfo> data, String type) {
        super(R.layout.weike_info_item, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeInfo item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_unit_count, item.getUserNum() + "");
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
        if (mType.equals("8")) {
            helper.setText(R.id.tv_learn_or_buy, "人已购买");

            if (item.getIsPay() == 0) {
                helper.setVisible(R.id.iv_is_vip, true);
            } else {
                helper.setVisible(R.id.iv_is_vip, false);
            }

        } else {
            helper.setVisible(R.id.iv_is_vip, false);
            helper.setText(R.id.tv_learn_or_buy, "已学习");
        }
    }
}
