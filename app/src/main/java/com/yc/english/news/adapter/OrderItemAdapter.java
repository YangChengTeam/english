package com.yc.english.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class OrderItemAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {

    public OrderItemAdapter(List<CourseInfo> data) {
        super(R.layout.order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {
        helper.setText(R.id.tv_course_title, item.getTitle()).setText(R.id.tv_course_price, "原价¥:" + item.getPrice()).setText(R.id.tv_course_price_now, "¥" + item.getMPrice());
    }
}
