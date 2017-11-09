package com.yc.english.weixin.views.adapters;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.WeiKeCategory;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKeCategoryItemAdapter extends BaseQuickAdapter<WeiKeCategory, BaseViewHolder> {
    private String mType;

    public WeiKeCategoryItemAdapter(List<WeiKeCategory> data, String type) {
        super(R.layout.weike_item_course, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_unit_count, item.getUnitNum() + "");
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
        if (mType.equals("8")) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
    }
}
