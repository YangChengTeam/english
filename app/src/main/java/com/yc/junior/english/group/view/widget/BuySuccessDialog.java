package com.yc.junior.english.group.view.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.yc.junior.english.R;


public class BuySuccessDialog extends Dialog {

    Button mConfirmButton;

    public BuySuccessDialog(Activity context) {
        super(context, R.style.customDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutID(), null);
        this.setContentView(view);

        this.setCancelable(true);
        view.setLayoutParams(new FrameLayout.LayoutParams((int)(ScreenUtils.getScreenWidth() * 0.8),FrameLayout.LayoutParams.WRAP_CONTENT));
        mConfirmButton = (Button) view.findViewById(R.id.btn_confirm);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySuccessDialog.this.dismiss();
            }
        });
    }

    public int getLayoutID() {
        return R.layout.buy_score_success;
    }
}
