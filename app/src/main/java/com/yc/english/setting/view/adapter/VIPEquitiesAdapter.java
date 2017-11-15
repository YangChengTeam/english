package com.yc.english.setting.view.adapter;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/8 16:28.
 */

public class VIPEquitiesAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public VIPEquitiesAdapter(List<Integer> data) {
        super(R.layout.activity_open_vip_days, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_open_first_day, item + "");
    }

}
