package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.model.engin.LoginEngin;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class LoginPresenter extends BasePresenter<LoginEngin, LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(Context context, LoginContract.View view) {
        super(view);
        mEngin = new LoginEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
