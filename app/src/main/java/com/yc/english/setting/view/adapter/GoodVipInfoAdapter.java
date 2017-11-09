package com.yc.english.setting.view.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.view.Listener.onItemClickListener;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/6 16:38.
 */

public class GoodVipInfoAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {

    private int lastPos = 0;
    private int currentPos;

    private SparseArray<CheckBox> sparseArray = new SparseArray<>();

    public GoodVipInfoAdapter(List<GoodInfo> data) {
        super(R.layout.activity_setting_vip_item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {

        helper.setText(R.id.tv_month_three, item.getName()).setText(R.id.tv_price, String.format(mContext.getString(R.string.price), item.getM_price()));
        int position = helper.getAdapterPosition();

        CheckBox checkBox = helper.getView(R.id.ck_three);
        if (position == mData.size() - 1) {
            helper.setVisible(R.id.view_divider, false);
        }
        sparseArray.put(position, checkBox);
        sparseArray.get(currentPos).setChecked(true);

        initListener();
    }

    private void initListener() {

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
              GoodInfo item = (GoodInfo) adapter.getItem(position);
                currentPos = position;
                if (lastPos != currentPos) {
                    sparseArray.get(lastPos).setChecked(false);
                }
                sparseArray.get(currentPos).setChecked(true);
                if (listener != null) {
                    listener.onItemClick(item);
                }
                lastPos = currentPos;
            }
        });

    }

    private onItemClickListener<GoodInfo> listener;

    public void setOnItemClickListener(onItemClickListener<GoodInfo> listener) {
        this.listener = listener;
    }

}
