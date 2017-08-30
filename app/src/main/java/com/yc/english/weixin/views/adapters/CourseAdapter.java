package com.yc.english.weixin.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
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
        int postion = helper.getAdapterPosition();
        helper.setText(R.id.tv_title, (postion + 1) + "." + item.getTitle());
        int margin = SizeUtils.dp2px(10);
        View view = helper.getConvertView();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (postion == getData().size() - 1) {
            layoutParams.setMargins(margin, margin, margin, margin);
        } else {
            layoutParams.setMargins(margin, margin, margin, 0);
        }
    }
}
