package com.yc.english.main.view.activitys;

import android.widget.Button;
import android.widget.EditText;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.ForgotContract;
import com.yc.english.main.presenter.ForgotPresenter;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotActivity extends FullScreenActivity<ForgotPresenter> implements ForgotContract.View {

    @BindView(R.id.btn_ok)
    Button mOkButton;

    @BindView(R.id.et_username)
    EditText mUsernameEditText;

    @BindView(R.id.et_password)
    EditText mPassEditText;

    @Override
    public void init() {
        mToolbar.setTitle("忘记密码");
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.main_activity_forgot;
    }
}
