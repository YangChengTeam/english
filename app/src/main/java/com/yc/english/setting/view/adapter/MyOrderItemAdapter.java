package com.yc.english.setting.view.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
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
                .setText(R.id.tv_good_name, item.getTitle())
                .setText(R.id.tv_order_date, TimeUtils.millis2String(item.getOrderAddTime() * 1000));

        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);

        String orderState = "";

        switch (item.getOrderStatus()) {
            case 0:
                orderState = "待支付";
                break;
            case 1:
                orderState = "支付失败";
                break;
            case 2:
                orderState = "支付成功";
                break;
            case 3:
                orderState = "发货失败";
                break;
            case 4:
                orderState = "交易完成";
                break;
            case 5:
                orderState = "申请退货";
                break;
            case 6:
                orderState = "退货失败";
                break;
            case 7:
                orderState = "退货成功";
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_order_state, orderState);
    }
}
