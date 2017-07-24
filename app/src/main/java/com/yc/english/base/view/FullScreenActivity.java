package com.yc.english.base.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;


import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.MainToolBar;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/24.
 */

public abstract class FullScreenActivity<P extends BasePresenter> extends BaseActivity<P> {

    @BindView(R.id.toolbar)
    protected MainToolBar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mToolbar == null) {
            throw new NullPointerException("error, please set com.yc.english.main.view.MainToolBar id -> toolbar.");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        mToolbar.init(this);
    }

}
