package com.yc.english.base.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.LogUtils;
import com.kk.utils.UIUitls;
import com.yc.english.base.utils.NavgationBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/28.
 */

public abstract class BasePopupWindow extends PopupWindow implements IView {
    protected Activity mContext;
    private ColorDrawable mBackgroundDrawable;



    public BasePopupWindow(Activity context){
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contextView = inflater.inflate(getLayoutId(), null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contextView);
        setWindowAlpha(0.5f);

        int aid = getAnimationID();
        if(aid != 0) {
           setAnimationStyle(aid);
        }

        try {
            ButterKnife.bind(this, contextView);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }

        init();
    }

    private void setWindowAlpha(float alpha){
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha =alpha;
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        UIUitls.postDelayed(300, new Runnable() {
            @Override
            public void run() {
                setWindowAlpha(1f);
            }
        });
    }

    public abstract int getAnimationID();

    @Override
    public void setContentView(View contentView) {
        if(contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public void show() {
        show(mContext.getWindow().getDecorView().getRootView());
    }

    public void show(View view) {
        show(view, Gravity.BOTTOM);
    }

    public void show(View view, int gravity) {
        showAtLocation(view, gravity, 0, NavgationBarUtils.getNavigationBarHeight(mContext));
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if(touchable) {
            if(mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }
}
