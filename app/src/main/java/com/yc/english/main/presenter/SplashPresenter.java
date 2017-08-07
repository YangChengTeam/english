package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.SplashContract;
import com.yc.english.main.model.engin.SplashEngin;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashPresenter extends BasePresenter<SplashEngin, SplashContract.View> implements SplashContract.Presenter {

    public SplashPresenter(Context context, SplashContract.View view) {
        super(context, view);
        mEngin = new SplashEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if(!forceUpdate) return;
        mView.gotToMain();
    }
}
