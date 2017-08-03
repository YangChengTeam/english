package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.model.engin.MainEngin;


/**
 * Created by zhangkai on 2017/7/20.
 */

public class MainPresenter extends BasePresenter<MainEngin, MainContract.View> implements MainContract.Presenter {

    private Context mContext;
    public MainPresenter(Context context, MainContract.View view) {
        super(context, view);
        mEngin = new MainEngin(context);
    }

    public void loadData(final boolean forceUpdate, final boolean showLoadingUI) {
        if(!forceUpdate) return;
    }

}
