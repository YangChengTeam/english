package com.yc.english.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.contract.MyContract;
import com.yc.english.setting.model.engin.MyEngin;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class MyPresenter extends BasePresenter<MyEngin, MyContract.View> implements MyContract.Presenter {

    public MyPresenter(Context context, MyContract.View iView) {
        super(context, iView);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if(!forceUpdate) return;
        getUserInfo();
    }


    @Override
    public void getUserInfo() {
        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showUserInfo(userInfo);
            }

            @Override
            public void showNoLogin() {
                mView.showNoLogin(true);
            }
        });
    }
}
