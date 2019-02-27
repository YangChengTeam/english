package com.yc.junior.english.weixin.views.adapters;

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


    public WeiKeCategoryItemAdapter(List<WeiKeCategory> data) {
        super(R.layout.weike_item_course, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_unit_count, item.getUnitNum() + "");
        helper.setImageResource(R.id.iv_tag, item.getTypeId().equals("8") ? R.mipmap.weike_tv : R.mipmap.weike_av);
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
    }
}
