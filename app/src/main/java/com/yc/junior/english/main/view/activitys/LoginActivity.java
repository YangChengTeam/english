package com.yc.junior.english.main.view.activitys;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.utils.StatusBarCompat;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.main.contract.LoginContract;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.presenter.LoginPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class LoginActivity extends FullScreenActivity<LoginPresenter> implements LoginContract.View {

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

    @BindView(R.id.imageView)
    ImageView bgImageView;

    @Override
    public void init() {
        mToolbar.setTitle("登录");
        mToolbar.showNavigationIcon();

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


        StatusBarCompat.transparentStatusBar(this);
        StatusBarCompat.compat(this, bgImageView, mToolbar, R.mipmap.main_login_top_bg);
    }

    

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_login;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.FINISH_LOGIN)
            }
    )
    public void finish(Boolean flag) {
        if (flag) {
            finish();
        }
    }

    @Override
    public void showPhone(String phone) {
        mUsernameEditText.setText(phone);
    }
}
