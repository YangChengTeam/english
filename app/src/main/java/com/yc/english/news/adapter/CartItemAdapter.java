package com.yc.english.news.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CartItemAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {

    public CartItemAdapter(List<CourseInfo> data) {
        super(R.layout.shopping_cart_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {
        helper.setText(R.id.tv_course_title, item.getTitle()).setText(R.id.tv_course_price, "原价¥:" + item.getPrice()).setText(R.id.tv_course_price_now, "¥"+item.getPayPrice());
        helper.addOnClickListener(R.id.ck_cart_item);

        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
        if (item.getIsChecked()) {
            helper.setChecked(R.id.ck_cart_item, true);
        } else {
            helper.setChecked(R.id.ck_cart_item, false);
        }
    }
}
