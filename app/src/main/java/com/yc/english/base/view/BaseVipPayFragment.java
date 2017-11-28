package com.yc.english.base.view;


import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/11/27 14:46.
 */

public class BaseVipPayFragment extends BaseFragment {


    @BindView(R.id.baseItemView_ceping)
    BasePayItemView baseItemViewCeping;
    @BindView(R.id.tv_vip_three_month)
    TextView tvVipThreeMonth;
    @BindView(R.id.tv_vip_six_month)
    TextView tvVipSixMonth;
    @BindView(R.id.tv_vip_tween_month)
    TextView tvVipTweenMonth;
    @BindView(R.id.tv_vip_forever)
    TextView tvVipForever;
    @BindView(R.id.iv_vip_price)
    ImageView ivVipPrice;
    @BindView(R.id.vip_current_price)
    TextView vipCurrentPrice;
    @BindView(R.id.vip_price_unit)
    TextView vipPriceUnit;
    @BindView(R.id.tv_vip_original_price)
    TextView tvVipOriginalPrice;
    @BindView(R.id.ll_vip_ali)
    LinearLayout llVipAli;
    @BindView(R.id.ll_vip_wx)
    LinearLayout llVipWx;
    private int mType;


    @Override
    public void init() {
        baseItemViewCeping.setVisibility(mType == 2 ? View.GONE : View.VISIBLE);
        tvVipThreeMonth.setBackgroundResource(R.drawable.vip_item_select_time);
        llVipAli.setBackgroundResource(R.drawable.vip_item_select_time);
        tvVipOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        initListener();

    }

    private void initListener() {
        RxView.clicks(tvVipThreeMonth).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                restoreTextViewBg();
                tvVipThreeMonth.setBackgroundResource(R.drawable.vip_item_select_time);

            }
        });
        RxView.clicks(tvVipSixMonth).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                restoreTextViewBg();
                tvVipSixMonth.setBackgroundResource(R.drawable.vip_item_select_time);

            }
        });
        RxView.clicks(tvVipTweenMonth).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                restoreTextViewBg();
                tvVipTweenMonth.setBackgroundResource(R.drawable.vip_item_select_time);
            }
        });
        RxView.clicks(tvVipForever).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                restoreTextViewBg();
                tvVipForever.setBackgroundResource(R.drawable.vip_item_select_time);
            }
        });
        RxView.clicks(llVipAli).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                llVipWx.setBackgroundResource(R.drawable.vip_item_unselect_time);
                llVipAli.setBackgroundResource(R.drawable.vip_item_select_time);
            }
        });
        RxView.clicks(llVipWx).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                llVipAli.setBackgroundResource(R.drawable.vip_item_unselect_time);
                llVipWx.setBackgroundResource(R.drawable.vip_item_select_time);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_vip_right;
    }


    private void restoreTextViewBg() {

        tvVipThreeMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipSixMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipTweenMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipForever.setBackgroundResource(R.drawable.vip_item_unselect_time);
    }

    public void setType(int type) {
        this.mType = type;
    }
}
