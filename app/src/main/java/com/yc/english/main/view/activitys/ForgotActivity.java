package com.yc.english.main.view.activitys;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.UIUitls;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.main.contract.ForgotContract;
import com.yc.english.main.presenter.ForgotPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotActivity extends FullScreenActivity<ForgotPresenter> implements ForgotContract.View {

    @BindView(R.id.btn_ok)
    Button mOkButton;

    @BindView(R.id.et_username)
    EditText mUsernameEditText;

    @BindView(R.id.et_password)
    EditText mPasswordEditText;

    @BindView(R.id.et_checkcode)
    EditText mCheckCodeEditText;

    @BindView(R.id.tv_checkcode)
    TextView mCheckCodeTextView;

    @BindView(R.id.imageView)
    ImageView bgImageView;

    private int mSecondes;


    @Override
    public void init() {
        mToolbar.setTitle("忘记密码");
        mToolbar.showNavigationIcon();

        mPresenter = new ForgotPresenter(this, this);

        RxView.clicks(mOkButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CharSequence phone = mUsernameEditText.getText().toString();
                CharSequence checkCode = mCheckCodeEditText.getText().toString();
                CharSequence password = mPasswordEditText.getText().toString();
                mPresenter.resetPassword(phone.toString(), password.toString(), checkCode.toString());
            }
        });

        RxView.clicks(mCheckCodeTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CharSequence phone = mUsernameEditText.getText().toString();
                mPresenter.sendCode(phone.toString());
            }
        });

        StatusBarCompat.transparentStatusBar(this);
        StatusBarCompat.compat(this, bgImageView, mToolbar, R.mipmap.main_login_top_bg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_forgot;
    }

    @Override
    public void codeRefresh() {
        mSecondes = 60;
        mCheckCodeTextView.setClickable(false);
        mCheckCodeTextView.setText("重新发送(" + mSecondes + ")");
        mCheckCodeTextView.setBackgroundColor(Color.GRAY);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSecondes-- <= 0) {
                    mCheckCodeTextView.setClickable(true);
                    mCheckCodeTextView.setBackgroundColor(ContextCompat.getColor(ForgotActivity.this, R.color.primary));
                    mCheckCodeTextView.setText("获取验证码");
                    return;
                }
                mCheckCodeTextView.setClickable(false);
                mCheckCodeTextView.setText("重新发送(" + mSecondes + ")");
                mCheckCodeTextView.setBackgroundColor(Color.GRAY);
                UIUitls.postDelayed(1000, this);
            }
        };
        UIUitls.postDelayed(1000, runnable);
    }
}
