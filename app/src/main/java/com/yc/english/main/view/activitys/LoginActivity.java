package com.yc.english.main.view.activitys;


import android.view.Menu;
import android.view.MenuItem;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.presenter.LoginPresenter;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class LoginActivity extends FullScreenActivity<LoginPresenter> implements LoginContract.View{
    @Override
    public void init() {
        mToolbar.setTitle("登录帐号");
    }

    @Override
    public int getLayoutID() {
        return R.layout.main_activity_login;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_login, menu);
        MenuItem menuItem=menu.findItem(R.id.action_register);
        return super.onCreateOptionsMenu(menu);
    }
}
