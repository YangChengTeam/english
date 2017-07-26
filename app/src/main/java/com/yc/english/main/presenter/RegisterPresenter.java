package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.RegisterContract;
import com.yc.english.main.model.engin.RegisterEngin;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class RegisterPresenter extends BasePresenter {

    public RegisterPresenter(Context context, RegisterContract.View view) {
        super(view);
        mEngin = new RegisterEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
