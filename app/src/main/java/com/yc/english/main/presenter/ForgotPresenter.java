package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.ForgotContract;
import com.yc.english.main.model.engin.ForgotEngin;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotPresenter extends BasePresenter {
    public ForgotPresenter(Context context, ForgotContract.View view) {
        super(view);
        mEngin = new ForgotEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
