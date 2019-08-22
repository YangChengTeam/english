package com.yc.english.main.view.fragments;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;

/**
 * Created by suns  on 2019/8/22 09:04.
 */
public class IndexNoticeFragment extends BaseDialogFragment {
    @BindView(R.id.tv_btn)
    TextView tvBtn;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index_notice;
    }

    @Override
    public void init() {
        RxView.clicks(tvBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

    }

    @Override
    protected float getWidth() {
        return 0.7f;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }
}
