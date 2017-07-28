package com.yc.english.main.view.activitys;

import android.content.Intent;

import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.RegisterContract;
import com.yc.english.main.presenter.RegisterPresenter;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class RegisterActivity extends FullScreenActivity<RegisterPresenter> implements RegisterContract.View{
    @Override
    public void init() {
        mToolbar.setTitle("注册帐号");
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_register;
    }
}
