package com.yc.english.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.utils.UIUitls;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog {
    protected P mPresenter;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        setContentView(getLayoutId());
        mLoadingDialog = new LoadingDialog(this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(this.getClass().getSimpleName() + " ButterKnife->初始化失败 原因:" + e);
        }
        init();
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
