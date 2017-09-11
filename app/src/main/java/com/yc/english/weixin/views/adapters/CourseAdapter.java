package com.yc.english.weixin.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        long addTime = Long.parseLong(item.getAdd_time()) * 1000;
        helper.setText(R.id.tv_time, TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd " +
                "HH:mm:ss",
                Locale.getDefault())));
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
    }
}
