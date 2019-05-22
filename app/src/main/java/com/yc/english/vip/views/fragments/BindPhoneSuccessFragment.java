package com.yc.english.vip.views.fragments;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2019/5/21 16:09.
 */
public class BindPhoneSuccessFragment extends BaseDialogFragment {
    @BindView(R.id.tv_know)
    TextView tvKnow;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_bind_phone_success;
    }

    @Override
    public void init() {
        RxView.clicks(tvKnow).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


}
