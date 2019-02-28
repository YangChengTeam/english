package com.yc.junior.english.base.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yc.junior.english.R;

import butterknife.ButterKnife;
import yc.com.base.IView;
import yc.com.blankj.utilcode.util.LogUtils;

/**
 * Created by zhangkai on 2017/7/28.
 */

public abstract class BaseDialog extends Dialog implements IView {

    public BaseDialog(Context context) {
        super(context, R.style.customDialog);
        View view = LayoutInflater.from(context).inflate(
                getLayoutId(), null);
        try {
            ButterKnife.bind(this, view);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }

        setContentView(view);
        setCancelable(true);

        init();
    }
}
