package com.yc.english.main.view.activitys;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.UIUitls;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.LoadingDialog;
import com.yc.english.main.contract.RegisterContract;
import com.yc.english.main.presenter.RegisterPresenter;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class RegisterActivity extends FullScreenActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.et_username)
    EditText mUsernameEditText;

    @BindView(R.id.et_password)
    EditText mPasswordEditText;

    @BindView(R.id.et_checkcode)
    EditText mCheckCodeEditText;

    @BindView(R.id.btn_regiter)
    Button mRegisterButton;

    @BindView(R.id.tv_checkcode)
    TextView mCheckCodeTextView;

    private LoadingDialog mLoadingDialog;
    private int mSecondes;

    @Override
    public void init() {
        mLoadingDialog = new LoadingDialog(this);
        mPresenter = new RegisterPresenter(this, this);

        mToolbar.setTitle("注册帐号");
        mToolbar.showNavigationIcon();

        RxView.clicks(mRegisterButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CharSequence phone = mUsernameEditText.getText().toString();
                CharSequence checkCode = mCheckCodeEditText.getText().toString();
                CharSequence password = mPasswordEditText.getText().toString();
                mPresenter.register(phone.toString(), checkCode.toString(), password.toString());
            }
        });

        RxView.clicks(mCheckCodeTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CharSequence phone = mUsernameEditText.getText().toString();
                mPresenter.sendCode(phone.toString());
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_register;
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
                    mCheckCodeTextView.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.primary));
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
