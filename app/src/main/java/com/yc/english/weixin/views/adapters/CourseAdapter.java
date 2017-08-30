package com.yc.english.weixin.views.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.weixin.model.CourseInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public CourseAdapter(List<CourseInfo> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {

    }
}
