package com.yc.english.main.view.activitys;

import android.app.Activity;
import android.widget.TextView;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BasePopupWindow;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class FollowWeiXinPopupWindow extends BasePopupWindow {

    @BindView(R.id.tv_ok)
    TextView mOkTextView;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;

    public FollowWeiXinPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public void init() {
        RxView.clicks(mOkTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
                ClipboardUtils.copyText("说说英语");
                SnackbarUtils.with(mContext.getWindow().getDecorView()).setMessage("复制成功").show();
            }
        });

        RxView.clicks(mCancelTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
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
