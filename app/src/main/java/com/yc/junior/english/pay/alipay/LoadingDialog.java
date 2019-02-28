package com.yc.junior.english.pay.alipay;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.junior.english.R;


/**
 * Created by zhangkai on 2017/2/21.
 */

public class LoadingDialog extends Dialog {

    ImageView ivCircle;
    TextView tvMsg;

    public LoadingDialog(Activity context) {

        super(context, R.style.customDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutID(), null);
        this.setContentView(view);

        this.setCancelable(true);
        ivCircle = view.findViewById(R.id.iv_circle);
        tvMsg = view.findViewById(R.id.tv_msg);
    }

    public void show(String msg) {
        super.show();
        ivCircle.startAnimation(AnimationUtil.rotaAnimation());
        tvMsg.setText(msg);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ivCircle.clearAnimation();

    }

    public int getLayoutID() {
        return R.layout.dialog_loading;
    }
}
