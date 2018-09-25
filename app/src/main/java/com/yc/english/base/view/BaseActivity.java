package com.yc.english.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.UIUitls;
import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.utils.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog {
    protected P mPresenter;
    protected LoadingDialog mLoadingDialog;
    protected int statusBarHeight;
    protected boolean hasChangeStatus = true;

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        statusBarHeight = BarUtils.getStatusBarHeight(this);
        LogUtils.e("statusBarHeight:  " + statusBarHeight);
        ScreenUtils.setPortrait(this);
        RxBus.get().register(this);
        setContentView(getLayoutId());
        mLoadingDialog = new LoadingDialog(this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }
        if (hasChangeStatus) {
            StatusBarCompat.transparentStatusBar(this);
        }
        init();
    }

    public void setToolbarTopMargin(View view) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusBarHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusBarHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusBarHeight, 0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
        RxBus.get().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void showLoadingDialog(String msg) {
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        UIUitls.post(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
