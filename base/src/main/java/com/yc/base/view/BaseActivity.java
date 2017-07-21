package com.yc.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.RxBus;
import com.yc.base.presenter.BasePresenter;

import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/7/17.
 */

public abstract class BaseActivity<P extends BasePresenter> extends FragmentActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
        try {
            setContentView(getLayoutID());
            ButterKnife.bind(this);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i(this.getClass().getSimpleName() + " initViews->初始化失败 原因:" + e);
        }

        init();

    }

    public abstract void init();

    public abstract int getLayoutID();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
    }
}
