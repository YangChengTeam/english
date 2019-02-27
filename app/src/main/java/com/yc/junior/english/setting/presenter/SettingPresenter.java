package com.yc.junior.english.setting.presenter;

import android.content.Context;

import com.yc.junior.english.base.helper.GlideCatchHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.setting.contract.SettingContract;
import com.yc.junior.english.setting.model.engin.MyEngin;

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
