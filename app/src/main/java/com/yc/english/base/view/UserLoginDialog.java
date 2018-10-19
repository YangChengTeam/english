package com.yc.english.base.view;

import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.yc.english.R;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2018/10/16 11:12.
 */
public class UserLoginDialog extends BaseDialog {
    @BindView(R.id.tv_tint_username)
    TextView tvTintUsername;
    @BindView(R.id.tv_modify_username)
    TextView tvModifyUsername;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_skip)
    TextView tvSkip;

    public UserLoginDialog(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void init() {

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ScreenUtil.getWidth(getContext()) * 3 / 4;
        getWindow().setAttributes(layoutParams);

        if (UserInfoHelper.isLogin()) {

            UserInfo userInfo = UserInfoHelper.getUserInfo();
            String tint = String.format(getContext().getString(R.string.username_pwd),
                    userInfo.getName(), userInfo.getPwd_text());

            tvTintUsername.setText(tint);

        }

        RxView.clicks(tvSkip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

        RxView.clicks(tvLogin).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfoHelper.isGotoLogin(getContext());
            }
        });

        RxView.clicks(tvModifyUsername).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_user_login;
    }
}
