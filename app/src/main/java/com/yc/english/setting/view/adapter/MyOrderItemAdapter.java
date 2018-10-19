package com.yc.english.setting.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.setting.model.bean.MyOrderInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class MyOrderItemAdapter extends BaseQuickAdapter<MyOrderInfo, BaseViewHolder> {

    public MyOrderItemAdapter(List<MyOrderInfo> data) {
        super(R.layout.my_order_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderInfo item) {
        helper.setText(R.id.tv_order_sn, item.getOrderSn()).
                setText(R.id.tv_good_id, item.getId())
                .setText(R.id.good_title, item.getTitle())
                .setText(R.id.tv_order_date, item.getAddTime())
                .setText(R.id.tv_order_state, item.getStatus())
        .setText(R.id.tv_good_price,"¥"+item.getOrderMoney());

//        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);

    }
}
