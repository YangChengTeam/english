package com.yc.english.setting.presenter;

import android.content.Context;

import com.yc.english.base.helper.GlideCatchHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.IView;
import com.yc.english.setting.contract.SettingContract;
import com.yc.english.setting.model.engin.MyEngin;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class SettingPresenter extends BasePresenter<MyEngin, SettingContract.View> implements SettingContract.Presenter {
    public SettingPresenter(Context context, SettingContract.View iView) {
        super(context, iView);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if(!forceUpdate) return;
        getCacheSize();
    }

    @Override
    public void getCacheSize() {
        mView.ShowCacheSize(GlideCatchHelper.getInstance(mContext).getCacheSize());
    }
}
