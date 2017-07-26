package com.yc.english.main.view.activitys;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.presenter.LoginPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class LoginActivity extends FullScreenActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.btn_login)
    Button mLoginButton;

    @Override
    public void init() {
        mToolbar.setTitle("登录帐号");

        RxView.clicks(mLoginButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
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
