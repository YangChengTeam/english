package com.yc.english.weixin.views.adapters;

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

public class CourseAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public CourseAdapter(List<CourseInfo> data) {
        super(R.layout.weixin_item_course, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_unit_count, "16");
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
    }
}
