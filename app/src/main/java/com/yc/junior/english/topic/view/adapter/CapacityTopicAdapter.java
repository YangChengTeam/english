package com.yc.junior.english.topic.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.topic.model.bean.TopicInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/9/29 16:55.
 */

public class CapacityTopicAdapter extends BaseQuickAdapter<TopicInfo ,BaseViewHolder> {
    public CapacityTopicAdapter(int layoutResId, List<TopicInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicInfo item) {

    }
}
