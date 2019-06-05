package com.yc.junior.english.setting.view.popupwindows;

import android.app.Activity;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.view.BasePopupWindow;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.AppUtils;
import yc.com.blankj.utilcode.util.ClipboardUtils;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class FollowWeiXinPopupWindow extends BasePopupWindow {

    @BindView(R.id.tv_ok)
    TextView mOkTextView;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    public FollowWeiXinPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public void init() {
        RxView.clicks(mOkTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            dismiss();
            ClipboardUtils.copyText("说说英语");
            TipsHelper.tips(mContext, "复制成功, 正在前往微信");
            UIUitls.postDelayed(1000, () -> {
                String weixin = "com.tencent.mm";
                if (AppUtils.isInstallApp(weixin)) {
                    AppUtils.launchApp(weixin);
                }
            });
        });

        RxView.clicks(mCancelTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());
        tvDesc.setText("已复制微信号: " + "说说英语" + "平台");
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_ppw_follow_weixin;
    }

    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }
}
