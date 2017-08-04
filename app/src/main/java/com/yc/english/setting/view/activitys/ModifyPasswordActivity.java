package com.yc.english.setting.view.activitys;

import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.setting.contract.ModifyPasswordContract;
import com.yc.english.setting.presenter.ModifyPasswordPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class ModifyPasswordActivity extends FullScreenActivity<ModifyPasswordPresenter> implements
        ModifyPasswordContract.View {

    @BindView(R.id.et_old_password)
    EditText mOldPasswordEditView;

    @BindView(R.id.et_new_password)
    EditText mNewPasswordEditView;

    @BindView(R.id.et_ok_password)
    EditText mOkPasswordEditView;

    @BindView(R.id.btn_complete)
    Button mCompleteButton;

    @Override
    public void init() {
        mToolbar.setTitle("修改密码");
        mToolbar.showNavigationIcon();



        RxView.clicks(mCompleteButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                KeyboardUtils.hideSoftInput(ModifyPasswordActivity.this);
                String oldPassword = mOldPasswordEditView.getText().toString();
                String newPassword = mNewPasswordEditView.getText().toString();
                String okPassword = mOkPasswordEditView.getText().toString();
                mPresenter.updatePassword(oldPassword, newPassword, okPassword);
            }
        });

        mPresenter = new ModifyPasswordPresenter(this , this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_modify_password;
    }
}
