package com.yc.junior.english.vip.views.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hwangjr.rxbus.RxBus;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.presenter.BasePresenter;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import yc.com.base.EmptyUtils;
import yc.com.base.IView;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.ScreenUtils;
import yc.com.blankj.utilcode.util.SizeUtils;

/**
 * Created by wanglin  on 2017/12/1 14:15.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements IView {

    public View rootView;
    protected P mPresenter;
    public Window window;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        RxBus.get().register(this);

        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            window = getDialog().getWindow();
            window.setGravity(getGravity());//((ViewGroup) window.findViewById(android.R.id.content))
            rootView = inflater.inflate(getLayoutId(), ((ViewGroup) window.findViewById(android.R.id.content)), false);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
//            window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight() * 3 / 5 + SizeUtils.dp2px(50));//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
            try {
                ButterKnife.bind(this, rootView);
            } catch (Exception e) {
                LogUtils.e("初始化失败-->" + e.getMessage());
            }
            init();
        }
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (ScreenUtil.getWidth(getActivity()) * getWidth());
            layoutParams.height = getHeight();
            window.setAttributes(layoutParams);
        }

    }

    protected float getWidth() {
        return 1.0f;
    }


    protected int getAnimationId() {
        return R.style.vip_style;
    }

    public int getHeight() {
        return ScreenUtils.getScreenHeight() * 3 / 5 + SizeUtils.dp2px(50);
    }

    protected int getGravity() {
        return Gravity.BOTTOM;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.unsubscribe();
        RxBus.get().unregister(this);
    }
}
