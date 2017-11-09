package com.yc.english.setting.view.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.pay.PayConfig;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.setting.view.Listener.onItemClickListener;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/7 11:22.
 */

public class GoodPayWayInfoAdapter extends BaseQuickAdapter<PayWayInfo, BaseViewHolder> {

    private SparseArray<CheckBox> checkBoxSparseArray = new SparseArray<>();
    private int lastPos;
    private int currentPos;

    public GoodPayWayInfoAdapter(List<PayWayInfo> data) {
        super(R.layout.activity_pay_way_item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayWayInfo item) {
        helper.setText(R.id.tv_alipay_way, item.getPay_way_title())
                .setImageResource(R.id.iv_alipay, item.getPay_way_name().equals(PayConfig.ali_pay) ? R.mipmap.alipay_icon :

                        item.getPay_way_name().equals(PayConfig.wx_pay) ? R.mipmap.weixin_icon : -1);
        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            helper.setVisible(R.id.view_divider, false);
        }
        CheckBox cb = helper.getView(R.id.ck_alipay);
        checkBoxSparseArray.put(position, cb);
        checkBoxSparseArray.get(currentPos).setChecked(true);
        initListener();
    }

    private void initListener() {
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PayWayInfo payWayInfo = (PayWayInfo) adapter.getItem(position);
                currentPos = position;
                if (lastPos != currentPos) {
                    checkBoxSparseArray.get(lastPos).setChecked(false);
                }
                checkBoxSparseArray.get(currentPos).setChecked(true);
                lastPos = currentPos;
                if (listener != null) {
                    listener.onItemClick(payWayInfo);
                }
            }
        });
    }

    private onItemClickListener<PayWayInfo> listener;

    public void setOnItemClickListener(onItemClickListener<PayWayInfo> listener) {
        this.listener = listener;
    }
}
