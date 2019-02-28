package com.yc.junior.english.news.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2018/10/18 13:56.
 */
public class QRCodeScanAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public QRCodeScanAdapter(List<String> items) {
        super(R.layout.textview_item, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item, item);
    }
}
