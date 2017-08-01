package com.yc.english.main.view.activitys;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.UIUitls;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.LoadingDialog;
import com.yc.english.main.contract.LoginContract;
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
    EditText mPassEditText;

    @BindView(R.id.tv_forgot)
    TextView mForgotTextView;

    @BindView(R.id.tv_register)
    TextView mRegisterTextView;

    @Override
    public void init() {
//        mToolbar.setTitle("登录帐号");
//        mToolbar.setMenuTitle("注册");
//        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
//            @Override
//            public void onClick() {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });

        RxView.clicks(mLoginButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                loadingDialog.setMessage("请稍后");
                loadingDialog.show();

                UIUitls.postDelayed(1000, new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                });
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
}
