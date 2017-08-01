package com.yc.english.main.view.activitys;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.presenter.LoginPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.btn_login)
    Button mLoginButton;

    @BindView(R.id.et_username)
    EditText mUsernameEditText;

    @BindView(R.id.et_password)
    EditText mPasswordEditText;

    @BindView(R.id.tv_forgot)
    TextView mForgotTextView;

    @BindView(R.id.tv_register)
    TextView mRegisterTextView;

    @Override
    public void init() {
        mPresenter = new LoginPresenter(this, this);

        RxView.clicks(mLoginButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CharSequence phone = mUsernameEditText.getText().toString();
                CharSequence password = mPasswordEditText.getText().toString();
                mPresenter.login(phone.toString(), password.toString());
            }
        });

        RxView.clicks(mForgotTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

        RxView.clicks(mRegisterTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_login;
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.MAIN)
            }
    )
    public void gotoMain(Boolean flag) {
        if (flag) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}
