package com.yc.junior.english.vip.adapter;

import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.setting.model.bean.GoodInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wanglin  on 2019/4/28 10:59.
 */
public class PayTimeLimitAdapter extends BaseQuickAdapter<GoodInfo, BaseViewHolder> {
    private SparseArray<View> views;

    public PayTimeLimitAdapter(@Nullable List<GoodInfo> data) {
        super(R.layout.pay_time_limit_item, data);
        views = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item) {
        BigDecimal bd = new BigDecimal(item.getPay_price());
        BigDecimal bd1 = new BigDecimal(item.getPrice());

        helper.setText(R.id.tv_current_price, String.valueOf(bd.stripTrailingZeros().intValue()))
                .setText(R.id.tv_origin_price, mContext.getString(R.string.new_origin_price, bd1.stripTrailingZeros().intValue()))
                .setText(R.id.tv_time_limit, item.getName());

        ((TextView) helper.getView(R.id.tv_origin_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        int position = helper.getAdapterPosition();
        if (position == 0) {
            helper.getView(R.id.rl_container).setSelected(true);
        }

        views.put(position, helper.getView(R.id.rl_container));
    }


    private void resetState() {
        if (views.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                views.get(i).setSelected(false);
            }
        }
    }

    public View getViewByPostion(int position) {
        resetState();
        return views.get(position);
    }
}
