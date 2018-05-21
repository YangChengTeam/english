package com.yc.junior.english.base.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class AlertDialog extends BaseDialog {

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_desc)
    TextView mDescTextView;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;

    @BindView(R.id.tv_ok)
    TextView mOkTextView;

    public AlertDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        setTitle("提示");
        RxView.clicks(mCancelTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });


        RxView.clicks(mOkTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (onClickListener != null) {
                    onClickListener.onClick(mOkTextView);
                }
                dismiss();
            }
        });
    }

    public View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setDesc(String desc) {
        mDescTextView.setText(desc);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_dialog_alert;
    }
}
