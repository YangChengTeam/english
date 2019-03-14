package com.yc.junior.english.main.presenter;

import android.content.Context;

import com.yc.junior.english.main.contract.MainContract;
import com.yc.junior.english.main.model.engin.MainEngin;

import yc.com.base.BasePresenter;


/**
 * Created by zhangkai on 2017/7/20.
 */

public class MainPresenter extends BasePresenter<MainEngin, MainContract.View> implements MainContract.Presenter {

    private Context mContext;
    public MainPresenter(Context context, MainContract.View view) {
        super(context, view);
        mEngine = new MainEngin(context);
    }

    public void loadData(final boolean forceUpdate, final boolean showLoadingUI) {
        if(!forceUpdate) return;
    }

}
