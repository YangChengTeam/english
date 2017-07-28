package com.yc.english.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/24.
 */

public abstract class BaseView extends FrameLayout {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        try {
            inflate(context, getLayoutId(), this);
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("baseView初始化失败->" + e);
        }
    }

    public abstract int getLayoutId();
}

