package com.yc.english.setting.presenter;

import android.content.Context;

import com.yc.english.base.helper.GlideCatchHelper;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.setting.contract.SettingContract;
import com.yc.english.setting.model.engin.MyEngin;

import yc.com.base.BasePresenter;

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
        if(UserInfoHelper.isPhoneLogin()){
            mView.showExitButton();
        }
    }

    @Override
    public void getCacheSize() {
        mView.ShowCacheSize(GlideCatchHelper.getInstance(mContext).getCacheSize());
    }
}
