package com.yc.junior.english.vip.views.fragments;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.vip.contract.BindPhoneContract;
import com.yc.junior.english.vip.presenter.BindPhonePresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseDialogFragment;
import yc.com.base.TipsHelper;

/**
 * Created by wanglin  on 2019/5/21 14:42.
 */
public class BindPhoneFragment extends BaseDialogFragment<BindPhonePresenter> implements BindPhoneContract.View {
    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_bind_phone)
    EditText etBindPhone;
    @BindView(R.id.et_bind_code)
    EditText etBindCode;
    @BindView(R.id.tv_bind_get_code)
    TextView tvBindGetCode;
    @BindView(R.id.et_bind_pwd)
    EditText etBindPwd;
    @BindView(R.id.et_bind_reset_pwd)
    EditText etBindResetPwd;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_bind_phone;
    }

    @Override
    public void init() {

        mPresenter = new BindPhonePresenter(getActivity(), this);

        RxView.clicks(tvBindPhone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = etBindPhone.getText().toString().trim();

                String code = etBindCode.getText().toString().trim();

                String pwd = etBindPwd.getText().toString().trim();

                String resetPwd = etBindResetPwd.getText().toString().trim();

                mPresenter.bindPhone(phone, code, pwd, resetPwd);

            }
        });

        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                TipsHelper.tips(getActivity(), "下次需要绑定可点击设置->绑定手机号，进行绑定",5000);
                dismiss();
            }
        });

        RxView.clicks(tvBindGetCode).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                String phone = etBindPhone.getText().toString().trim();
                mPresenter.sendCode(phone);
            }
        });
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected float getWidth() {
        return 0.85f;
    }

    @Override
    public void showBindSuccess() {
        dismiss();
        BindPhoneSuccessFragment bindPhoneSuccessFragment = new BindPhoneSuccessFragment();
        bindPhoneSuccessFragment.show(getActivity().getSupportFragmentManager(), "");
    }

    @Override
    public void senCodeSuccess() {
        ((BaseActivity) getActivity()).showGetCodeDisplay(tvBindGetCode);
    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }

}
