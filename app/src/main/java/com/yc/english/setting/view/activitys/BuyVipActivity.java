package com.yc.english.setting.view.activitys;

import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/10/31.
 */

public class BuyVipActivity extends FullScreenActivity {

    @BindView(R.id.layout_three_month)
    RelativeLayout mThreeMonthLayout;

    @BindView(R.id.layout_sex_month)
    RelativeLayout mSexMonthLayout;

    @BindView(R.id.layout_alipay)
    RelativeLayout mAlipayLayout;

    @BindView(R.id.layout_weixin)
    RelativeLayout mWeiXinLayout;

    @BindView(R.id.ck_three)
    CheckBox mThreeCkBox;

    @BindView(R.id.ck_sex)
    CheckBox mSexCkBox;

    @BindView(R.id.ck_alipay)
    CheckBox mAlipayCkBox;

    @BindView(R.id.ck_weixin)
    CheckBox mWeiXinBox;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    public void init() {
        mToolbar.setTitle("VIP会员");
        mToolbar.showNavigationIcon();

        RxView.clicks(mThreeMonthLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mSexCkBox.setChecked(false);
                mThreeCkBox.setChecked(true);
            }
        });
        RxView.clicks(mSexMonthLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mThreeCkBox.setChecked(false);
                mSexCkBox.setChecked(true);
            }
        });

        RxView.clicks(mAlipayLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mWeiXinBox.setChecked(false);
                mAlipayCkBox.setChecked(true);
            }
        });
        RxView.clicks(mWeiXinLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mAlipayCkBox.setChecked(false);
                mWeiXinBox.setChecked(true);
            }
        });

    }
}
