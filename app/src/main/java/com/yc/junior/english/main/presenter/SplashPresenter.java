package com.yc.junior.english.main.presenter;

import android.content.Context;

import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.main.contract.SplashContract;
import com.yc.junior.english.main.model.engin.SplashEngin;

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
